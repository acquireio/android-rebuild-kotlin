package com.acquire.sdk.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.acquire.sdk.app.HelpActivity
import com.acquire.sdk.app.util.CheckAccountIDTask
import com.acquire.sdk.app.util.SelectedTab
import com.acquire.sdk.app.util.SucessCallBack
import com.acquireio.AcquireApp
import com.acquireio.AcquireApp.init
import com.acquireio.AcquireApp.logOut
import com.acquireio.AcquireApp.setSessionListener
import com.acquireio.AcquireApp.startDirectSupportChat
import com.acquireio.callbacks.OnSessionEvents
import com.acquireio.enums.CallType

class HelpActivity : BaseActivity(), OnSessionEvents {
    private var txtSdkStatus: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        txtSdkStatus = findViewById(R.id.txtSdkStatus)
    }

    override fun onResume() {
        super.onResume()
        selectedTab(SelectedTab.HELP)
        AppComponent.AccID.also { (findViewById<View>(R.id.txtAccId) as TextView).text = it }
    }

    fun changeAccId(view: View?) {
        val layoutInflaterAndroid = LayoutInflater.from(this@HelpActivity)
        val mView = layoutInflaterAndroid.inflate(R.layout.user_input, null)
        val alertDialogBuilderUserInput = AlertDialog.Builder(this@HelpActivity)
        alertDialogBuilderUserInput.setView(mView)
        val userInputDialogEditText = mView.findViewById<View>(R.id.userInputDialog) as EditText
        alertDialogBuilderUserInput.setCancelable(true).setPositiveButton("Change") { dialogBox, id ->

            CheckAccountIDTask(this, object : SucessCallBack {
                override fun sucess(b: Boolean) {
                    if (b) {
                        val prefs = getSharedPreferences("Acquire_sdk", MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.clear()
                        editor.putString("acc_id", userInputDialogEditText.text.toString())
                        editor.apply()
                        Handler().postDelayed({
                            val mStartActivity = Intent(this@HelpActivity.applicationContext, HelpActivity::class.java)
                            val mPendingIntentId = 123456
                            val mPendingIntent = PendingIntent
                                    .getActivity(this@HelpActivity, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
                            val mgr = this@HelpActivity.getSystemService(ALARM_SERVICE) as AlarmManager
                            mgr[AlarmManager.RTC, System.currentTimeMillis() + 50] = mPendingIntent
                            System.exit(0)
                        }, 500)
                    } else {
                        changeAccId(null)
                    }
                }
            }).execute(userInputDialogEditText.text.toString().trim { it <= ' ' })
        }.setNegativeButton("Cancel") { dialogBox, id -> dialogBox.cancel() }
        val alertDialogAndroid = alertDialogBuilderUserInput.create()
        alertDialogAndroid.show()
    }

    /**
     * Method to place a direct audio call by calling sdk function
     */
    fun placeAudioCall(view: View?) {
        if (AcquireApp != null) startDirectSupportChat(CallType.AUDIO)
    }

    /**
     * Method to place a direct video call by calling sdk function
     */
    fun placeVideoCall(view: View?) {
        if (AcquireApp != null) startDirectSupportChat(CallType.VIDEO)
    }

    /**
     * Method to logout by calling sdk function
     */
    fun doLogout(view: View?) {
        val success = MutableLiveData<Boolean>()
        if (txtSdkStatus!!.text.toString() == "Disconnected") {
            Toast.makeText(this, "SDK not connected", Toast.LENGTH_SHORT).show()
            return
        }
        success.observe(this, Observer { aBoolean -> if (aBoolean) txtSdkStatus!!.text = "Disconnected" })
        logOut(success)
    }

    /**
     * Method to restart sdk using sdk init function
     */
    fun doRestart(view: View?) {
        init(this.application, AppComponent.Companion.AccID!!)
        setSessionListener(this)
    }

    override fun onSessionConnected() {
        runOnUiThread { txtSdkStatus!!.text = "Connected" }
    }

    override fun onSessionDisconnected(s: String?) {
        runOnUiThread { txtSdkStatus!!.text = "Disconnected" }
    }

    override fun onAgentOnline() {}
    override fun onAgentOffline() {}
    override fun onAgentAvailable() {}
    override fun onCallConnected() {}
    override fun onTriggerEvent(s: String?) {}
    override fun onChatClosed() {}
    override fun onChatWidgetClose() {}
    override fun onTagChange(list: List<String?>?) {}
    override fun noAgentAvailable() {}
    override fun onWaitDialogAppear() {}
    override fun onWaitDialogDisappear() {}
    override fun onCallDisconnected() {}
}