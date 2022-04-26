package com.sweatworks.datagraphics.poc.custom.charts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.sweatworks.datagraphics.poc.R
import com.sweatworks.datagraphics.poc.custom.renderers.RoundBarChartRender
import com.sweatworks.datagraphics.poc.databinding.BarChartViewBinding

class BarChartView (context: Context,
    attrs: AttributeSet?,
@AttrRes defStyleAttr: Int,
@StyleRes defStyleRes: Int
): FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context) : this(context, null, 0, 0)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0) {
        init(attrs)
    }

    private val binding = BarChartViewBinding.inflate(LayoutInflater.from(context), this, false)
    private val chartData = ArrayList<Int>()
    private val chartDataEntries = ArrayList<BarEntry>()
    private var barWidth = 0.2f
    private var barColor = ContextCompat.getColor(context, R.color.green_speede)
    private var barGradientStartColor = ContextCompat.getColor(context, R.color.green_speede_start_bar_color)
    private var barGradientEndColor = ContextCompat.getColor(context, R.color.green_speede_end_bar_color)
    private var showGradient = false

    private fun init(attrs: AttributeSet?) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.BarChartView)

        try {
            barWidth = a.getFloat(R.styleable.BarChartView_barWidth, barWidth)
            barColor = a.getColor(R.styleable.BarChartView_barColor, barColor)
            barGradientStartColor = a.getColor(R.styleable.BarChartView_barGradientStartColor, barGradientStartColor)
            barGradientEndColor = a.getColor(R.styleable.BarChartView_barGradientEndColor, barGradientEndColor)
            showGradient = a.getBoolean(R.styleable.BarChartView_showGradient, showGradient)
        } finally {
            a.recycle()
        }

        setChartStyle()
        addView(binding.root)
    }

    private fun setChartStyle() {
        val chart = binding.chart
        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        chart.isDragEnabled = true
        chart.setDrawGridBackground(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(0f, 10f, 0f, 0f)

        // add nice and smooth animation
        chart.animateY(1500)

        chart.legend.isEnabled = false

        addChartData(0)

        val renderer = RoundBarChartRender(chart, chart.animator, chart.viewPortHandler)
        renderer.setRadius(20)
        chart.renderer = renderer

        chart.invalidate()
    }

    fun addChartData(value: Int) {
        chartData.add(value)
        drawBars(value)
    }

    fun addChartData(values: List<Int>) {
        chartData.addAll(values)
        values.forEach { drawBars(it) }
    }

    fun clearChartData() {
        chartData.clear()
        chartDataEntries.clear()
        binding.chart.clearValues()
    }

    private fun drawBars(value: Int) {
        val multi = value + 1
        val entryData = (Math.random() * multi) + multi / 3
        chartDataEntries.add(BarEntry(chartDataEntries.size + 1f, entryData.toFloat()))

        val chart = binding.chart
        val set1: BarDataSet

        if (chart.data != null &&
            chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = chartDataEntries
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(chartDataEntries, "Data Set")
            if (showGradient) {
                set1.setGradientColor(
                    barGradientEndColor,
                    barGradientStartColor
                )
            } else {
                set1.color = barColor
            }
            set1.setDrawValues(false)

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            chart.data = data
            chart.setFitBars(true)
            chart.barData.barWidth = barWidth
        }
        chart.invalidate()
    }
}
