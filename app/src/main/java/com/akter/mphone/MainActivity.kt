package com.akter.mphone

import android.Manifest
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.akter.mphone.database.*
import com.akter.mphone.security.CredentialVault
import com.akter.mphone.sip.SipEngineManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.UUID

val CyanWave = Color(0xFF38D9E8)
val CallGreen = Color(0xFF34C759)
val EndCallRed = Color(0xFFFF3B30)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MPhoneRoot() }
    }
}

@Composable
fun GlassBox(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun MPhoneRoot() {
    val context = LocalContext.current
    val sip = remember { SipEngineManager.getInstance(context) }
    val db = remember { MPhoneDatabase.getInstance(context) }
    val vault = remember { CredentialVault(context) }
    val scope = rememberCoroutineScope()

    val incoming by sip.incomingCallModel.collectAsState()
    val active by sip.activeCallModel.collectAsState()
    val summary by sip.lastCompletedCallLog.collectAsState()
    val accounts by db.sipAccountDao().observeAllAccounts().collectAsState(initial = emptyList())
    val statuses by sip.registrationStatuses.collectAsState()
    val logs by db.callLogDao().observeAllLogs().collectAsState(initial = emptyList())

    var tab by remember { mutableIntStateOf(0) }
    var screen by remember { mutableStateOf("MAIN") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
    LaunchedEffect(Unit) {
        val p = mutableListOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS)
        if (Build.VERSION.SDK_INT >= 33) p.add(Manifest.permission.POST_NOTIFICATIONS)
        if (Build.VERSION.SDK_INT >= 31) p.add(Manifest.permission.BLUETOOTH_CONNECT)
        launcher.launch(p.toTypedArray())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0A2233), Color(0xFF1B3B5E), Color(0xFF4A2E7A), Color(0xFF1D6B7D))
                )
            )
    ) {
        when {
            incoming != null -> {
                val c = incoming!!
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(40.dp))
                        Text("Incoming on ${c.sipAccountName}", color = CyanWave, fontSize = 14.sp)
                        Text(c.callerName, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text(c.phoneNumber, color = Color.White.copy(0.8f), fontSize = 18.sp)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        IconButton(
                            onClick = { sip.rejectIncomingCall() },
                            modifier = Modifier.size(72.dp).clip(CircleShape).background(EndCallRed)
                        ) { Icon(Icons.Default.CallEnd, null, tint = Color.White) }
                        IconButton(
                            onClick = { sip.acceptIncomingCall() },
                            modifier = Modifier.size(72.dp).clip(CircleShape).background(CallGreen)
                        ) { Icon(Icons.Default.Call, null, tint = Color.White) }
                    }
                }
            }
            active != null -> {
                val c = active!!
                var sec by remember { mutableIntStateOf(0) }
                var muted by remember { mutableStateOf(false) }
                var held by remember { mutableStateOf(false) }
                var rec by remember { mutableStateOf(c.isRecording) }
                LaunchedEffect(Unit) { while (true) { delay(1000); sec++ } }

                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(36.dp))
                        Text("${c.sipAccountName} • ${c.stateText}", color = CyanWave, fontSize = 13.sp)
                        Text(c.callerName, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Text(String.format("%02d:%02d", sec / 60, sec % 60), color = Color.White, fontSize = 18.sp)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { muted = sip.toggleMute() }) { Text(if (muted) "Unmute" else "Mute") }
                        Button(onClick = { held = sip.toggleHold() }) { Text(if (held) "Resume" else "Hold") }
                        Button(onClick = { rec = sip.toggleManualRecording() }) { Text(if (rec) "Stop Rec" else "Record") }
                    }
                    IconButton(
                        onClick = { sip.hangupActiveCall() },
                        modifier = Modifier.size(72.dp).clip(CircleShape).background(EndCallRed)
                    ) { Icon(Icons.Default.CallEnd, null, tint = Color.White) }
                }
            }
            screen == "ACCOUNTS" -> {
                var showAdd by remember { mutableStateOf(false) }
                Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp).verticalScroll(rememberScrollState())) {
                    IconButton(onClick = { screen = "MAIN" }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
                    Text("SIP Accounts", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    accounts.forEach { acc ->
                        val st = statuses[acc.id]?.state ?: acc.lastRegistrationStatus
                        val err = statuses[acc.id]?.errorDetail ?: acc.lastErrorDetail
                        GlassBox(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(acc.accountName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                    Text("${acc.username}@${acc.domain}", color = Color.White.copy(0.75f), fontSize = 13.sp)
                                    Text(st, color = if (st == "Active") CallGreen else CyanWave, fontSize = 13.sp)
                                    if (err.isNotBlank()) Text(err, color = EndCallRed, fontSize = 11.sp)
                                }
                                IconButton(onClick = {
                                    scope.launch {
                                        vault.deleteSipPassword(acc.id)
                                        db.sipAccountDao().deleteAccount(acc)
                                        sip.syncAllAccountsFromDatabase()
                                    }
                                }) { Icon(Icons.Default.Delete, null, tint = EndCallRed) }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth()) { Text("+ Add SIP Account") }
                }

                if (showAdd) {
                    var name by remember { mutableStateOf("BanglaCall") }
                    var user by remember { mutableStateOf("") }
                    var pass by remember { mutableStateOf("") }
                    var dom by remember { mutableStateOf("") }
                    var port by remember { mutableStateOf("5060") }
                    var trans by remember { mutableStateOf("UDP") }
                    var autoRec by remember { mutableStateOf(true) }

                    Dialog(onDismissRequest = { showAdd = false }) {
                        GlassBox {
                            Text("New SIP Account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Account Name") })
                            OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("SIP Username") })
                            OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("SIP Password") }, visualTransformation = PasswordVisualTransformation())
                            OutlinedTextField(value = dom, onValueChange = { dom = it }, label = { Text("SIP Domain / Server") })
                            OutlinedTextField(value = port, onValueChange = { port = it }, label = { Text("Port") })
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("UDP", "TCP", "TLS").forEach { t ->
                                    FilterChip(selected = trans == t, onClick = { trans = t }, label = { Text(t) })
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Auto Record Calls", color = Color.White, modifier = Modifier.weight(1f))
                                Switch(checked = autoRec, onCheckedChange = { autoRec = it })
                            }
                            Button(
                                onClick = {
                                    if (user.isNotBlank() && dom.isNotBlank()) {
                                        val id = UUID.randomUUID().toString()
                                        scope.launch {
                                            vault.saveSipPassword(id, pass)
                                            db.sipAccountDao().upsertAccount(
                                                SipAccountEntity(
                                                    id = id,
                                                    accountName = name,
                                                    username = user.trim(),
                                                    domain = dom.trim(),
                                                    port = port.toIntOrNull() ?: 5060,
                                                    transport = trans,
                                                    autoRecordIncoming = autoRec,
                                                    autoRecordOutgoing = autoRec
                                                )
                                            )
                                            sip.syncAllAccountsFromDatabase()
                                        }
                                        showAdd = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Save & Register") }
                        }
                    }
                }
            }
            screen == "RECORDINGS" -> {
                val dir = File(context.getExternalFilesDir(null), "mPhone_Recordings")
                var files by remember { mutableStateOf(dir.listFiles()?.toList() ?: emptyList()) }
                var mp by remember { mutableStateOf<MediaPlayer?>(null) }
                Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) {
                    IconButton(onClick = { mp?.release(); screen = "MAIN" }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
                    Text("Call Recordings", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    files.forEach { f ->
                        GlassBox(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(f.name, color = Color.White, modifier = Modifier.weight(1f), fontSize = 13.sp)
                                IconButton(onClick = {
                                    mp?.release()
                                    mp = MediaPlayer().apply { setDataSource(f.absolutePath); prepare(); start() }
                                }) { Icon(Icons.Default.PlayArrow, null, tint = CallGreen) }
                                IconButton(onClick = { f.delete(); files = dir.listFiles()?.toList() ?: emptyList() }) {
                                    Icon(Icons.Default.Delete, null, tint = EndCallRed)
                                }
                            }
                        }
                    }
                }
            }
            screen == "DIAGNOSTICS" -> {
                var dnsInfo by remember { mutableStateOf("Checking...") }
                LaunchedEffect(accounts) {
                    val dom = accounts.firstOrNull()?.domain ?: ""
                    dnsInfo = if (dom.isBlank()) "No SIP Domain" else try {
                        InetAddress.getByName(dom).hostAddress ?: "Resolved"
                    } catch (e: Exception) { "Failed: ${e.message}" }
                }
                val localIp = remember {
                    NetworkInterface.getNetworkInterfaces()?.toList()?.flatMap { it.inetAddresses.toList() }
                        ?.firstOrNull { !it.isLoopbackAddress && it is Inet4Address }?.hostAddress ?: "Unknown"
                }
                Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) {
                    IconButton(onClick = { screen = "MAIN" }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
                    Text("Network & SIP Diagnostics", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    GlassBox(Modifier.fillMaxWidth()) {
                        Text("Local IP: $localIp", color = Color.White)
                        Text("SIP Server DNS: $dnsInfo", color = CyanWave)
                    }
                }
            }
            else -> {
                Scaffold(
                    containerColor = Color.Transparent,
                    bottomBar = {
                        NavigationBar(containerColor = Color.White.copy(alpha = 0.14f)) {
                            listOf("Keypad" to Icons.Default.Dialpad, "Recents" to Icons.Default.History, "Settings" to Icons.Default.Settings).forEachIndexed { idx, pair ->
                                NavigationBarItem(
                                    selected = tab == idx,
                                    onClick = { tab = idx },
                                    icon = { Icon(pair.second, null) },
                                    label = { Text(pair.first) }
                                )
                            }
                        }
                    }
                ) { pad ->
                    Box(Modifier.padding(pad)) {
                        when (tab) {
                            0 -> {
                                var num by remember { mutableStateOf("") }
                                var pickAcc by remember { mutableStateOf(false) }
                                Column(
                                    Modifier.fillMaxSize().padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(num.ifEmpty { "Enter SIP Number" }, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                                        if (num.isNotEmpty()) {
                                            IconButton(onClick = { num = num.dropLast(1) }) {
                                                Icon(Icons.AutoMirrored.Filled.Backspace, null, tint = Color.White)
                                            }
                                        }
                                    }
                                    val keys = listOf(listOf("1","2","3"), listOf("4","5","6"), listOf("7","8","9"), listOf("*","0","#"))
                                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                        keys.forEach { r ->
                                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                                r.forEach { k ->
                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .size(74.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.White.copy(0.2f))
                                                            .clickable { num = "$num$k" }
                                                    ) { Text(k, color = Color.White, fontSize = 28.sp) }
                                                }
                                            }
                                        }
                                    }
                                    IconButton(
                                        onClick = { if (num.isNotEmpty()) pickAcc = true },
                                        modifier = Modifier.size(74.dp).clip(CircleShape).background(CallGreen)
                                    ) { Icon(Icons.Default.Call, null, tint = Color.White, modifier = Modifier.size(34.dp)) }
                                }
                                if (pickAcc) {
                                    Dialog(onDismissRequest = { pickAcc = false }) {
                                        GlassBox {
                                            Text("Select Account to Call", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                            if (accounts.isEmpty()) {
                                                Button(onClick = { pickAcc = false; screen = "ACCOUNTS" }) { Text("Add SIP Account First") }
                                            } else {
                                                accounts.forEach { a ->
                                                    TextButton(onClick = { pickAcc = false; sip.placeCall(num, a.id) }) {
                                                        Text("${a.accountName} (${statuses[a.id]?.state ?: a.lastRegistrationStatus})", color = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    item { Text("Recent Calls & Analytics", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold) }
                                    items(logs) { l ->
                                        GlassBox(Modifier.fillMaxWidth()) {
                                            Text("${l.callerName} (${l.direction})", color = Color.White, fontWeight = FontWeight.Bold)
                                            Text("${l.sipAccountName} • ${l.durationSeconds}s • Quality: ${l.qualityRating}", color = CyanWave, fontSize = 12.sp)
                                            Text("RTT: ${l.rtpRttMs ?: "N/A"} ms | Jitter: ${l.jitterMs ?: "N/A"} ms | Loss: ${l.packetLossPercent ?: "N/A"}%", color = Color.White.copy(0.8f), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                            2 -> {
                                Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Settings", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                                    GlassBox(Modifier.fillMaxWidth().clickable { screen = "ACCOUNTS" }) {
                                        Text("SIP Account Edit", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                        Text("Configure multiple SIP accounts", color = Color.White.copy(0.75f), fontSize = 13.sp)
                                    }
                                    GlassBox(Modifier.fillMaxWidth().clickable { screen = "RECORDINGS" }) {
                                        Text("Call Recordings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                        Text("Play & manage recorded calls", color = Color.White.copy(0.75f), fontSize = 13.sp)
                                    }
                                    GlassBox(Modifier.fillMaxWidth().clickable { screen = "DIAGNOSTICS" }) {
                                        Text("Network & SIP Diagnostics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                        Text("Check IP & SIP DNS reachability", color = Color.White.copy(0.75f), fontSize = 13.sp)
                                    }
                                    GlassBox(Modifier.fillMaxWidth()) {
                                        Text("About mPhone v1.0.0", color = Color.White, fontWeight = FontWeight.Bold)
                                        Text("This App Is Made With AI", color = CallGreen, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (summary != null) {
            val s = summary!!
            Dialog(onDismissRequest = { sip.clearLastCompletedCallLog() }) {
                GlassBox {
                    Text("Call Summary & Analytics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("${s.callerName} • ${s.sipAccountName}", color = CyanWave)
                    Text("Duration: ${s.durationSeconds}s | Network: ${s.networkType}", color = Color.White)
                    Text("Codec: ${s.codecUsed} | Quality: ${s.qualityRating}", color = CallGreen)
                    Text("RTT: ${s.rtpRttMs ?: "Unavailable"} ms | Jitter: ${s.jitterMs ?: "Unavailable"} ms", color = Color.White)
                    Text("Packet Loss: ${s.packetLossPercent ?: "Unavailable"}% | MOS: ${s.mosScore ?: "Unavailable"}", color = Color.White)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = { sip.clearLastCompletedCallLog() }, modifier = Modifier.fillMaxWidth()) { Text("Close") }
                }
            }
        }
    }
}
