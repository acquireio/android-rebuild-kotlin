package com.acquire.sdk.app

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.acquire.sdk.app.util.SelectedTab
import com.acquireio.AcquireApp
import com.acquireio.AcquireApp.startSupportChat

/**
 * Base class of all Activities of the Demo Application.
 *
 * @author Nilay Dani
 */
abstract class BaseActivity : AppCompatActivity() {
    private var selectedTab: SelectedTab? = null

    fun backPressed(view: View?) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity)
    }

    fun onClickEvent(view: View) {
        val tag = SelectedTab.valueOf(view.tag.toString())
        if (tag == selectedTab) return
        selectedTab = tag
        when (tag) {
            SelectedTab.HOME -> startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            SelectedTab.HELP -> startActivity(Intent(this, HelpActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    fun selectedTab(tab: SelectedTab?) {
        val ll = findViewById<View>(R.id.llHome) as LinearLayout
        (ll.getChildAt(0) as ImageView).setColorFilter(Color.parseColor("#B3B3B3"), PorterDuff.Mode.SRC_ATOP)
        (ll.getChildAt(1) as TextView).setTextColor(Color.parseColor("#B3B3B3"))
        val relativeLayout = findViewById<RelativeLayout>(R.id.rlBottomBar)
        selectedTab = tab
        when (selectedTab) {
            SelectedTab.HOME -> {
                val linearLayout = relativeLayout.getChildAt(0) as LinearLayout
                (linearLayout.getChildAt(0) as ImageView).setColorFilter(Color.parseColor("#585AFD"), PorterDuff.Mode.SRC_ATOP)
                (linearLayout.getChildAt(1) as TextView).setTextColor(Color.parseColor("#585AFD"))
            }
            SelectedTab.TEST -> {
                val linearLayout1 = relativeLayout.getChildAt(1) as LinearLayout
                (linearLayout1.getChildAt(0) as ImageView).setColorFilter(Color.parseColor("#585AFD"), PorterDuff.Mode.SRC_ATOP)
                (linearLayout1.getChildAt(1) as TextView).setTextColor(Color.parseColor("#585AFD"))
            }
            SelectedTab.SETTINGS -> {
                val linearLayout2 = relativeLayout.getChildAt(2) as LinearLayout
                (linearLayout2.getChildAt(0) as ImageView).setColorFilter(Color.parseColor("#585AFD"), PorterDuff.Mode.SRC_ATOP)
                (linearLayout2.getChildAt(1) as TextView).setTextColor(Color.parseColor("#585AFD"))
            }
            SelectedTab.HELP -> {
                val linearLayout3 = relativeLayout.getChildAt(3) as LinearLayout
                (linearLayout3.getChildAt(0) as ImageView).setColorFilter(Color.parseColor("#585AFD"), PorterDuff.Mode.SRC_ATOP)
                (linearLayout3.getChildAt(1) as TextView).setTextColor(Color.parseColor("#585AFD"))
            }
        }
    }

    fun startSDK(view: View?) {
        if (AcquireApp != null) {
            startSupportChat()
        }
    }
}