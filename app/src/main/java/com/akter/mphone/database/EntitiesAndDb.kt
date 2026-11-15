package com.akter.mphone.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Entity(tableName = "sip_accounts")
data class SipAccountEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val accountName: String,
    val username: String,
    val domain: String,
    val port: Int = 5060,
    val transport: String = "UDP",
    val outboundProxy: String = "",
    val authId: String = "",
    val displayName: String = "",
    val stunServer: String = "stun.l.google.com:19302",
    val registrationExpiry: Int = 300,
    val isEnabled: Boolean = true,
    val isDefaultOutgoing: Boolean = false,
    val autoRecordIncoming: Boolean = false,
    val autoRecordOutgoing: Boolean = false,
    val lastRegistrationStatus: String = "Not Registered",
    val lastErrorDetail: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val callerName: String,
    val phoneNumber: String,
    val sipAccountName: String,
    val direction: String,
    val callStatus: String,
    val startTime: Long,
    val endTime: Long,
    val durationSeconds: Int,
    val jitterMs: Float?,
    val packetLossPercent: Float?,
    val rtpRttMs: Float?,
    val networkType: String,
    val codecUsed: String,
    val totalDataBytes: Long,
    val mosScore: Float?,
    val qualityRating: String,
    val recordingFilePath: String? = null
)

@Dao
interface SipAccountDao {
    @Query("SELECT * FROM sip_accounts ORDER BY createdAt ASC")
    fun observeAllAccounts(): Flow<List<SipAccountEntity>>

    @Query("SELECT * FROM sip_accounts WHERE isEnabled = 1")
    suspend fun getEnabledAccounts(): List<SipAccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAccount(account: SipAccountEntity)

    @Delete
    suspend fun deleteAccount(account: SipAccountEntity)

    @Query("UPDATE sip_accounts SET lastRegistrationStatus = :status, lastErrorDetail = :error WHERE id = :id")
    suspend fun updateRegistrationState(id: String, status: String, error: String)
}

@Dao
interface CallLogDao {
    @Query("SELECT * FROM call_logs ORDER BY startTime DESC")
    fun observeAllLogs(): Flow<List<CallLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(log: CallLogEntity)
}

@Database(entities = [SipAccountEntity::class, CallLogEntity::class], version = 1, exportSchema = false)
abstract class MPhoneDatabase : RoomDatabase() {
    abstract fun sipAccountDao(): SipAccountDao
    abstract fun callLogDao(): CallLogDao

    companion object {
        @Volatile private var INSTANCE: MPhoneDatabase? = null
        fun getInstance(context: Context): MPhoneDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MPhoneDatabase::class.java,
                    "mphone_core.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
