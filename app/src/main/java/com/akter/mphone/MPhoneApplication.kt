package com.akter.mphone

import android.app.Application
import com.akter.mphone.service.MPhoneForegroundService
import com.akter.mphone.sip.SipEngineManager

class MPhoneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SipEngineManager.getInstance(this).initialize()
        MPhoneForegroundService.start(this)
    }
}
