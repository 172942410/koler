package com.lianyun.lxdphone

import android.telecom.TelecomManager
import androidx.preference.PreferenceManager
import com.perry.lib.ChoolooApp
import com.perry.lib.notification.CallNotification
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
open class KolerApp : ChoolooApp() {
    @Inject lateinit var telecomManager: TelecomManager
    @Inject lateinit var callNotification: CallNotification

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.setDefaultValues(this, R.xml.preferences_koler, false)
    }
}