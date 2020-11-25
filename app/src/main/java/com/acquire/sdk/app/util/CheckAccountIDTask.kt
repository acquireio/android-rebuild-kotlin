package com.acquire.sdk.app.util

import android.app.Activity
import android.os.AsyncTask
import android.widget.Toast
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CheckAccountIDTask(private val mActivity: Activity, private val mCallBack: SucessCallBack) : AsyncTask<String?, Void?, Void?>() {
    private val URL = "https://app.acquire.io/api/auth/account?id="

    override fun doInBackground(vararg strings: String?): Void? {
        if (strings.isNotEmpty())
            strings[0]?.let { checkAccountID(it) }
        return null
    }

    private fun checkAccountID(accId: String) {
        val url: URL
        var urlConnection: HttpURLConnection? = null
        try {
            url = URL(URL + accId)
            urlConnection = url.openConnection() as HttpURLConnection
            val `in` = urlConnection!!.inputStream
            val isw = InputStreamReader(`in`)
            var data = isw.read()
            val response = StringBuilder()
            while (data != -1) {
                val current = data.toChar()
                data = isw.read()
                response.append(current)
            }
            val jsonObject = JSONObject(response.toString())
            val success = jsonObject.optBoolean("success")
            mActivity.runOnUiThread {
                mCallBack.sucess(success)
                if (success) {
                    Toast.makeText(mActivity, "Please wait until app re-launches.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(mActivity, "Account Id does not exist.\nPlease check your account Id.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
        }
    }


}