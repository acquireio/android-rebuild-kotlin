package com.acquire.sdk.app

import android.app.Application
import com.acquireio.AcquireApp.init
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory

/**
 * Application class for our demo app.
 *
 * @author Nilay Dani
 */
class AppComponent : Application() {
    override fun onCreate() {
        super.onCreate()

        // Use the Sentry DSN (client key) from the Project Settings page on Sentry
        val sentryDsn = "https://9eeb88b8e4d1448db8de85a16ef4387f@sentry.io/1248245"
        Sentry.init(sentryDsn, AndroidSentryClientFactory(this))
        val prefs = getSharedPreferences("Acquire_sdk", MODE_PRIVATE)
        val editor = prefs.edit()
        val restoredText = prefs.getString("acc_id", null)
        if (restoredText != null) {
            AccID = prefs.getString("acc_id", "")
        } else {
            AccID = "d5885" //TODO Set Default Acc Id here
            editor.putString("acc_id", AccID)
            editor.apply()
        }
        init(this, AccID!!)
    }

    companion object {
        var AccID: String? = null
    }
}