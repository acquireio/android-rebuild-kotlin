package com.acquire.sdk.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.acquire.sdk.app.SplashActivity
import com.acquire.sdk.app.util.CheckAccountIDTask
import com.acquire.sdk.app.util.SucessCallBack

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (AppComponent.Companion.AccID != null) {
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 1000)
        } else {
            showInputDialog()
        }
    }

    private fun showInputDialog() {
        val layoutInflaterAndroid = LayoutInflater.from(this@SplashActivity)
        val mView = layoutInflaterAndroid.inflate(R.layout.user_input_splash, null)
        val alertDialogBuilderUserInput = AlertDialog.Builder(this@SplashActivity)
        alertDialogBuilderUserInput.setView(mView)
        val userInputDialogEditText = mView.findViewById<EditText>(R.id.userInputDialog)
        alertDialogBuilderUserInput.setCancelable(true).setPositiveButton("Change", DialogInterface.OnClickListener { dialogBox, id ->
            if (userInputDialogEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SplashActivity, "Account id can't be blank", Toast.LENGTH_LONG).show()
                showInputDialog()
                return@OnClickListener
            }

            CheckAccountIDTask(this, object : SucessCallBack {
                override fun sucess(b: Boolean) {
                    if (b) {
                        launchApplication(userInputDialogEditText.text.toString().trim { it <= ' ' })
                    } else {
                        showInputDialog()
                    }
                }
            }).execute(userInputDialogEditText.text.toString().trim { it <= ' ' })
        }).setNegativeButton("Cancel") { dialogBox, id ->
            dialogBox.cancel()
            finish()
        }
        val alertDialogAndroid = alertDialogBuilderUserInput.create()
        alertDialogAndroid.setCancelable(false)
        alertDialogAndroid.setCanceledOnTouchOutside(false)
        alertDialogAndroid.show()
    }

    private fun launchApplication(accID: String) {
        val prefs = getSharedPreferences("Acquire_sdk", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.putString("acc_id", accID)
        editor.apply()
        Handler().postDelayed({
            val mStartActivity = Intent(this@SplashActivity, SplashActivity::class.java)
            val mPendingIntentId = 123456
            val mPendingIntent = PendingIntent.getActivity(this@SplashActivity, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
            val mgr = this@SplashActivity.getSystemService(ALARM_SERVICE) as AlarmManager
            mgr[AlarmManager.RTC, System.currentTimeMillis() + 50] = mPendingIntent
            System.exit(0)
        }, 500)
    }

    fun openAcquire(view: View?) {
        val url = "https://app.acquire.io/signup/create-account"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}