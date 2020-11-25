package com.acquire.sdk.app

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.acquire.sdk.app.adapter.ViewPagerAdapter
import com.acquire.sdk.app.fragment.ChartFrag
import com.acquire.sdk.app.util.SelectedTab
import com.google.android.material.tabs.TabLayout

/**
 * Details activity .
 *
 * @author Nilay Dani
 */
class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initView()
    }

    override fun onResume() {
        super.onResume()
        selectedTab(SelectedTab.HOME)
    }

    private fun initView() {
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        setupViewPager(viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        if (intent.extras!!.getBoolean("leads")) {
            viewPager.currentItem = 0
        } else {
            viewPager.currentItem = 1
        }
    }

    //Setting View Pager
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        for (tab in tabArray) adapter.addFrag(ChartFrag.Companion.newInstance(tab), tab)
        viewPager.adapter = adapter
    }

    companion object {
        private val tabArray = arrayOf("Leads", "Returning Visitors") //Tab title array
    }
}