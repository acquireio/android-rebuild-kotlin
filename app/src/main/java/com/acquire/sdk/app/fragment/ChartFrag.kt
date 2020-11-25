package com.acquire.sdk.app.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acquire.sdk.app.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import java.util.*

/**
 * Fragment for showing chart.
 *
 * @author Nilay Dani
 */
class ChartFrag : Fragment() {
    private var rootView: View? = null
    private var chart: BarChart? = null
    private var title //String for tab title
            : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //fetch the title from passed arguments
        title = requireArguments().getString(TAB_TITLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.chat_fragment, container, false)
        chart = rootView?.findViewById(R.id.chart1)
        //        chart.setOnChartValueSelectedListener(this);
        chart?.description?.isEnabled = false

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart?.setPinchZoom(false)
        chart?.setDrawBarShadow(false)
        chart?.setDrawGridBackground(false)
        setChartData()

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(chart); // For bounds control
//        chart.setMarker(mv); // Set the marker to the chart
        return rootView
    }

    private fun setChartData() {
        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
        val groupCount = 10 + 1
        val startYear = 2000
        val endYear = startYear + groupCount

//        tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));
//        tvY.setText(String.valueOf(100));
        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        val values3 = ArrayList<BarEntry>()
        val values4 = ArrayList<BarEntry>()
        val randomMultiplier = 100 * 100000f
        for (i in startYear until endYear) {
            val pos = i.toFloat()
            values1.add(BarEntry(pos, (Math.random() * randomMultiplier).toFloat()))
            values2.add(BarEntry(pos, (Math.random() * randomMultiplier).toFloat()))
            values3.add(BarEntry(pos, (Math.random() * randomMultiplier).toFloat()))
            values4.add(BarEntry(pos, (Math.random() * randomMultiplier).toFloat()))
        }
        val set1: BarDataSet
        val set2: BarDataSet
        val set3: BarDataSet
        val set4: BarDataSet
        if (chart!!.data != null && chart!!.data.dataSetCount > 0) {
            set1 = chart!!.data.getDataSetByIndex(0) as BarDataSet
            set2 = chart!!.data.getDataSetByIndex(1) as BarDataSet
            set3 = chart!!.data.getDataSetByIndex(2) as BarDataSet
            set4 = chart!!.data.getDataSetByIndex(3) as BarDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            set4.values = values4
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            // create 4 DataSets
            set1 = BarDataSet(values1, "Company A")
            set1.color = Color.rgb(104, 241, 175)
            set2 = BarDataSet(values2, "Company B")
            set2.color = Color.rgb(164, 228, 251)
            set3 = BarDataSet(values3, "Company C")
            set3.color = Color.rgb(242, 247, 158)
            set4 = BarDataSet(values4, "Company D")
            set4.color = Color.rgb(255, 102, 0)
            val data = BarData(set1, set2, set3, set4)
            data.setValueFormatter(LargeValueFormatter())
            data.setDrawValues(false)
            chart!!.data = data
        }

        // specify the width each bar should have
        chart!!.barData.barWidth = barWidth

        // restrict the x-axis range
        chart!!.xAxis.axisMinimum = startYear.toFloat()

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart!!.xAxis.axisMaximum = startYear + chart!!.barData.getGroupWidth(groupSpace, barSpace) * groupCount
        chart!!.groupBars(startYear.toFloat(), groupSpace, barSpace)
        chart!!.invalidate()
    }

    companion object {
        private const val TAB_TITLE = "tab_title"

        /**
         * static instance of ChartFrag
         * in this method we will pass the title of our selected tab via Bundle
         *
         * @param title tab title
         */
        fun newInstance(title: String?): ChartFrag {
            val args = Bundle()
            args.putString(TAB_TITLE, title)
            val fragment = ChartFrag()
            fragment.arguments = args
            return fragment
        }
    }
}