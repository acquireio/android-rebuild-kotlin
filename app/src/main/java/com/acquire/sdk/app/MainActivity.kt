package com.acquire.sdk.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.acquire.sdk.app.util.SelectedTab
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*

/**
 * Launcher activity of our Application.
 *
 * @author Nilay Dani
 */
class MainActivity : BaseActivity(), OnSeekBarChangeListener, OnChartGestureListener, OnChartValueSelectedListener {
    private var chart: LineChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX?.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY?.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart?.setOnChartValueSelectedListener(this)
        chart?.setDrawGridBackground(false)
        chart?.description?.isEnabled = false
        chart?.setDrawBorders(false)
        chart?.axisLeft?.isEnabled = false
        chart?.axisRight?.setDrawAxisLine(false)
        chart?.axisRight?.setDrawGridLines(false)
        val xAxis = chart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawAxisLine(false)
        xAxis?.setDrawGridLines(true)
        xAxis?.enableGridDashedLine(10f, 10f, 10f)

        // enable touch gestures
        chart?.setTouchEnabled(true)

        // enable scaling and dragging
        chart?.isDragEnabled = true
        chart?.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart?.setPinchZoom(false)
        seekBarX?.progress = 5
        seekBarY?.progress = 10
        val l = chart?.legend
        l?.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l?.orientation = Legend.LegendOrientation.HORIZONTAL
        l?.setDrawInside(false)
        l?.xEntrySpace = 7f
        l?.yEntrySpace = 0f
        l?.yOffset = 10f
        l?.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        l?.isWordWrapEnabled = true
        chart?.description?.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        selectedTab(SelectedTab.HOME)
    }

    private val colors = intArrayOf(Color.parseColor("#58FFA5"), Color.parseColor("#585AFF"), Color.parseColor("#00E0FF"))
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        var progress = progress
        chart!!.resetTracking()
        progress = seekBarX!!.progress
        val dataSets = ArrayList<ILineDataSet>()
        for (z in 0..2) {
            val values = ArrayList<Entry>()
            for (i in 0 until progress) {
                val `val` = Math.random() * seekBarY!!.progress + 3
                values.add(Entry(i.toFloat(), `val`.toFloat()))
            }
            val d = LineDataSet(values, "DataSet " + (z + 1))
            d.lineWidth = 2.5f
            d.circleRadius = 4f
            d.mode = LineDataSet.Mode.CUBIC_BEZIER
            val color = colors[z % colors.size]
            d.color = color
            d.setCircleColor(color)
            dataSets.add(d)
        }

        // make the first DataSet dashed
        (dataSets[0] as LineDataSet).enableDashedLine(10f, 10f, 0f)
        (dataSets[0] as LineDataSet).setColors(*ColorTemplate.VORDIPLOM_COLORS)
        (dataSets[0] as LineDataSet).setCircleColors(*ColorTemplate.VORDIPLOM_COLORS)
        val data = LineData(dataSets)
        chart!!.data = data
        chart!!.invalidate()
    }

    override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartGesture) {
        Log.i("Gesture", "START, x: " + me.x + ", y: " + me.y)
    }

    override fun onChartGestureEnd(me: MotionEvent, lastPerformedGesture: ChartGesture) {
        Log.i("Gesture", "END, lastGesture: $lastPerformedGesture")

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartGesture.SINGLE_TAP) chart!!.highlightValues(null) // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    override fun onChartLongPressed(me: MotionEvent) {
        Log.i("LongPress", "Chart long pressed.")
    }

    override fun onChartDoubleTapped(me: MotionEvent) {
        Log.i("DoubleTap", "Chart double-tapped.")
    }

    override fun onChartSingleTapped(me: MotionEvent) {
        Log.i("SingleTap", "Chart single-tapped.")
    }

    override fun onChartFling(me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) {
        Log.i("Fling", "Chart fling. VelocityX: $velocityX, VelocityY: $velocityY")
    }

    override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {
        Log.i("Scale / Zoom", "ScaleX: $scaleX, ScaleY: $scaleY")
    }

    override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
        Log.i("Translate / Move", "dX: $dX, dY: $dY")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i("VAL SELECTED", "Value: " + e.y + ", xIndex: " + e.x + ", DataSet index: " + h.dataSetIndex)
    }

    override fun onNothingSelected() {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    fun openDetail(view: View) {
        if (view.tag.toString().equals("leads", ignoreCase = true)) {
            startActivity(Intent(this, DetailActivity::class.java).putExtra("leads", true))
        } else {
            startActivity(Intent(this, DetailActivity::class.java).putExtra("leads", false))
        }
    }
}