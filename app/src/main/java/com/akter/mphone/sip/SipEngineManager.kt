package com.akter.mphone.sip

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.akter.mphone.database.CallLogEntity
import com.akter.mphone.database.MPhoneDatabase
import com.akter.mphone.database.SipAccountEntity
import com.akter.mphone.security.CredentialVault
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.linphone.core.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

data class AccountRegStatus(
    val accountId: String,
    val state: String,
    val errorDetail: String = ""
)

data class LiveCallUiModel(
    val callerName: String,
    val phoneNumber: String,
    val sipAccountName: String,
    val direction: String,
    val stateText: String,
    val isRecording: Boolean
)

class SipEngineManager private constructor(private val context: Context) {
    private var core: Core? = null
    private val db = MPhoneDatabase.getInstance(context)
    private val vault = CredentialVault(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val accountMap = mutableMapOf<Account, SipAccountEntity>()
    private var currentRecordingFile: File? = null
    private var callStartTimeMs: Long = 0L

    private val _registrationStatuses = MutableStateFlow>(emptyMap())
    val registrationStatuses: StateFlow> = _registrationStatuses.asStateFlow()

    private val _incomingCallModel = MutableStateFlow<LiveCallUiModel?>(null)
    val incomingCallModel: StateFlow<LiveCallUiModel?> = _incomingCallModel.asStateFlow()

    private val _activeCallModel = MutableStateFlow<LiveCallUiModel?>(null)
    val activeCallModel: StateFlow<LiveCallUiModel?> = _activeCallModel.asStateFlow()

    private val _lastCompletedCallLog = MutableStateFlow<CallLogEntity?>(null)
    val lastCompletedCallLog: StateFlow<CallLogEntity?> = _lastCompletedCallLog.asStateFlow()

    private var currentIncomingCall: Call? = null
    private var currentActiveCall: Call? = null

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            val entity = accountMap[account] ?: return
            val statusText = when (state) {
                RegistrationState.Ok -> "Active"
                RegistrationState.Progress -> "Registering..."
                RegistrationState.Failed -> "Failed"
                else -> "Not Registered"
            }
            val err = if (state == RegistrationState.Failed) message else ""
            scope.launch { db.sipAccountDao().updateRegistrationState(entity.id, statusText, err) }
            val map = _registrationStatuses.value.toMutableMap()
            map[entity.id] = AccountRegStatus(entity.id, statusText, err)
            _registrationStatuses.value = map
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            val entity = accountMap.entries.firstOrNull { (_, e) ->
                call.toAddress.username == e.username || call.callLog?.fromAddress?.username == e.username
            }?.value
            val accName = entity?.accountName ?: "SIP Account"
            val number = call.remoteAddress.username ?: "Unknown"
            val name = call.remoteAddress.displayName?.takeIf { it.isNotBlank() } ?: number
            val dir = if (call.dir == Call.Dir.Incoming) "INCOMING" else "OUTGOING"

            when (state) {
                Call.State.IncomingReceived -> {
                    currentIncomingCall = call
                    callStartTimeMs = System.currentTimeMillis()
                    _incomingCallModel.value = LiveCallUiModel(name, number, accName, "INCOMING", "Incoming Call", false)
                }
                Call.State.OutgoingInit, Call.State.OutgoingProgress, Call.State.OutgoingRinging -> {
                    currentActiveCall = call
                    if (callStartTimeMs == 0L) callStartTimeMs = System.currentTimeMillis()
                    _activeCallModel.value = LiveCallUiModel(name, number, accName, "OUTGOING", "Ringing...", currentRecordingFile != null)
                }
                Call.State.Connected, Call.State.StreamsRunning -> {
                    currentIncomingCall = null
                    currentActiveCall = call
                    _incomingCallModel.value = null
                    callStartTimeMs = System.currentTimeMillis()
                    val autoRec = if (dir == "INCOMING") entity?.autoRecordIncoming == true else entity?.autoRecordOutgoing == true
                    if (autoRec && currentRecordingFile == null) {
                        currentRecordingFile = createRecFile(number, dir)
                        call.startRecording()
                    }
                    _activeCallModel.value = LiveCallUiModel(name, number, accName, dir, "Connected", currentRecordingFile != null)
                }
                Call.State.End, Call.State.Error, Call.State.Released -> {
                    val recPath = currentRecordingFile?.absolutePath
                    if (currentRecordingFile != null) {
                        try { call.stopRecording() } catch (_: Exception) {}
                        currentRecordingFile = null
                    }
                    val end = System.currentTimeMillis()
                    val start = if (callStartTimeMs > 0L) callStartTimeMs else end
                    callStartTimeMs = 0L
                    val dur = max(0, ((end - start) / 1000L).toInt())
                    val stats = call.audioStats
                    val jitter = stats?.receiverInterarrivalJitter?.takeIf { it >= 0f }?.let { it * 1000f }
                    val loss = stats?.receiverLossRate?.takeIf { it >= 0f }
                    val rtt = stats?.roundTripDelay?.takeIf { it >= 0f }?.let { it * 1000f }
                    val mos = call.currentQuality.takeIf { it >= 0f }
                    val rating = when {
                        mos == null -> "Unavailable"
                        mos >= 4.0f -> "Excellent"
                        mos >= 3.0f -> "Good"
                        mos >= 2.0f -> "Fair"
                        else -> "Poor"
                    }
                    val log = CallLogEntity(
                        callerName = name,
                        phoneNumber = number,
                        sipAccountName = accName,
                        direction = dir,
                        callStatus = if (dur > 0) "COMPLETED" else if (dir == "INCOMING") "MISSED" else "FAILED",
                        startTime = start,
                        endTime = end,
                        durationSeconds = dur,
                        jitterMs = jitter,
                        packetLossPercent = loss,
                        rtpRttMs = rtt,
                        networkType = getNetType(),
                        codecUsed = call.currentParams?.usedAudioPayloadType?.mimeType ?: "Unknown",
                        totalDataBytes = (((stats?.downloadBandwidth ?: 0f) + (stats?.uploadBandwidth ?: 0f)) * 125f * dur).toLong(),
                        mosScore = mos,
                        qualityRating = rating,
                        recordingFilePath = recPath
                    )
                    scope.launch { db.callLogDao().insertCallLog(log) }
                    _lastCompletedCallLog.value = log
                    if (currentIncomingCall == call) { currentIncomingCall = null; _incomingCallModel.value = null }
                    if (currentActiveCall == call) { currentActiveCall = null; _activeCallModel.value = null }
                }
                else -> {}
            }
        }
    }

    fun clearLastCompletedCallLog() { _lastCompletedCallLog.value = null }

    fun initialize() {
        if (core != null) return
        val factory = Factory.instance()
        val lc = factory.createCore(null, null, context)
        lc.addListener(coreListener)
        lc.isIpv6Enabled = false
        lc.isEchoCancellationEnabled = true
        lc.start()
        core = lc
        scope.launch {
            while (isActive) {
                withContext(Dispatchers.Main) { core?.iterate() }
                delay(20L)
            }
        }
        scope.launch { syncAllAccountsFromDatabase() }
    }

    suspend fun syncAllAccountsFromDatabase() {
        val lc = core ?: return
        val accounts = db.sipAccountDao().getEnabledAccounts()
        withContext(Dispatchers.Main) {
            lc.clearAccounts()
            lc.clearAllAuthInfo()
            accountMap.clear()
            accounts.forEach { entity ->
                val factory = Factory.instance()
                lc.addAuthInfo(factory.createAuthInfo(entity.username, entity.authId.ifBlank { null }, vault.getSipPassword(entity.id), null, null, entity.domain))
                val params = lc.createAccountParams()
                val idAddr = factory.createAddress("sip:${entity.username}@${entity.domain}")
                if (entity.displayName.isNotBlank()) idAddr?.displayName = entity.displayName
                params.identityAddress = idAddr
                val srvUri = if (entity.outboundProxy.isNotBlank()) "sip:${entity.outboundProxy}:${entity.port}" else "sip:${entity.domain}:${entity.port}"
                val srvAddr = factory.createAddress(srvUri)
                srvAddr?.transport = when (entity.transport.uppercase()) {
                    "TCP" -> TransportType.Tcp
                    "TLS" -> TransportType.Tls
                    else -> TransportType.Udp
                }
                params.serverAddress = srvAddr
                params.expires = entity.registrationExpiry
                params.isRegisterEnabled = entity.isEnabled
                if (entity.stunServer.isNotBlank()) {
                    val nat = lc.createNatPolicy()
                    nat.stunServer = entity.stunServer
                    nat.isStunEnabled = true
                    params.natPolicy = nat
                }
                val acc = lc.createAccount(params)
                lc.addAccount(acc)
                if (entity.isDefaultOutgoing) lc.defaultAccount = acc
                accountMap[acc] = entity
            }
        }
    }

    fun placeCall(number: String, accountId: String? = null) {
        val lc = core ?: return
        val entry = accountMap.entries.firstOrNull { it.value.id == accountId } ?: accountMap.entries.firstOrNull() ?: return
        lc.defaultAccount = entry.key
        val clean = number.trim()
        val addr = lc.interpretUrl(if (clean.startsWith("sip:")) clean else "sip:$clean@${entry.value.domain}") ?: return
        val params = lc.createCallParams(null) ?: return
        if (entry.value.autoRecordOutgoing) {
            val f = createRecFile(clean, "OUTGOING")
            params.recordFile = f.absolutePath
            currentRecordingFile = f
        }
        callStartTimeMs = System.currentTimeMillis()
        lc.inviteAddressWithParams(addr, params)
    }

    fun acceptIncomingCall() {
        val call = currentIncomingCall ?: return
        val params = core?.createCallParams(call) ?: return
        call.acceptWithParams(params)
    }

    fun rejectIncomingCall() {
        currentIncomingCall?.decline(Reason.Declined)
        currentIncomingCall = null
        _incomingCallModel.value = null
    }

    fun hangupActiveCall() {
        currentActiveCall?.terminate() ?: core?.terminateAllCalls()
        currentActiveCall = null
        _activeCallModel.value = null
    }

    fun toggleMute(): Boolean {
        val lc = core ?: return false
        lc.isMicEnabled = !lc.isMicEnabled
        return !lc.isMicEnabled
    }

    fun toggleHold(): Boolean {
        val call = currentActiveCall ?: return false
        return if (call.state == Call.State.Paused) { call.resume(); false } else { call.pause(); true }
    }

    fun toggleManualRecording(): Boolean {
        val call = currentActiveCall ?: return false
        val m = _activeCallModel.value ?: return false
        return if (currentRecordingFile != null) {
            call.stopRecording()
            currentRecordingFile = null
            _activeCallModel.value = m.copy(isRecording = false)
            false
        } else {
            val f = createRecFile(m.phoneNumber, m.direction)
            currentRecordingFile = f
            call.startRecording()
            _activeCallModel.value = m.copy(isRecording = true)
            true
        }
    }

    fun sendDtmf(digit: Char) { currentActiveCall?.sendDtmf(digit) }

    private fun createRecFile(num: String, dir: String): File {
        val folder = File(context.getExternalFilesDir(null), "mPhone_Recordings").apply { mkdirs() }
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File(folder, "${dir}_${num}_${ts}.wav")
    }

    private fun getNetType(): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return "Offline"
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile Data"
            else -> "Other"
        }
    }

    companion object {
        @Volatile private var INSTANCE: SipEngineManager? = null
        fun getInstance(context: Context): SipEngineManager =
            INSTANCE ?: synchronized(this) { INSTANCE ?: SipEngineManager(context.applicationContext).also { INSTANCE = it } }
    }
}
