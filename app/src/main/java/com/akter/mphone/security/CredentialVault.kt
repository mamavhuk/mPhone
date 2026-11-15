package com.akter.mphone.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class CredentialVault(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val securePrefs = EncryptedSharedPreferences.create(
        context,
        "mphone_secure_sip_vault",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSipPassword(accountId: String, plainPassword: String) {
        securePrefs.edit().putString("sip_pwd_$accountId", plainPassword).apply()
    }

    fun getSipPassword(accountId: String): String =
        securePrefs.getString("sip_pwd_$accountId", "") ?: ""

    fun deleteSipPassword(accountId: String) {
        securePrefs.edit().remove("sip_pwd_$accountId").apply()
    }
}
