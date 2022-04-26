package com.sweatworks.datagraphics.poc.custom.charts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.sweatworks.datagraphics.poc.R
import com.sweatworks.datagraphics.poc.custom.MyMarkerView
import com.sweatworks.datagraphics.poc.custom.renderers.LineChartRenderer
import com.sweatworks.datagraphics.poc.databinding.LineChartViewBinding

class LineChartView(
    context: Context,
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

    private val binding: LineChartViewBinding = LineChartViewBinding.inflate(LayoutInflater.from(context), this, false)

    private val listener = object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {}
        override fun onNothingSelected() {}
    }

    private val renderer: LineChartRenderer = LineChartRenderer(binding.chart, binding.chart.animator, binding.chart.viewPortHandler)
    private val chartData = ArrayList<Int>()
    private val chartDataEntries = ArrayList<Entry>()
    var lineMode: LineDataSet.Mode = LineDataSet.Mode.LINEAR
    private var lineWidth: Float = 6f
    private var circleRadius: Float = 6f
    private var showCircle: Boolean = false
    private var showCircleHole: Boolean = false
    private var showDashedLine: Boolean = false
    var showFilledArea: Boolean = false
    private var chartRange: Float = 180f
    private var lineColor = ContextCompat.getColor(context, R.color.green_speede)
    private var circleColor = ContextCompat.getColor(context, R.color.green_speede)
    private var gradientColor = ContextCompat.getDrawable(context, R.drawable.fade_green)
    private var highlightColor = ContextCompat.getColor(context, R.color.white)

    private fun init(attrs: AttributeSet?) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.LineChartView)

        try {
            lineMode = when(a.getInt(R.styleable.LineChartView_lineMode, 0)) {
                1 -> LineDataSet.Mode.STEPPED
                2 -> LineDataSet.Mode.CUBIC_BEZIER
                3 -> LineDataSet.Mode.HORIZONTAL_BEZIER
                else -> LineDataSet.Mode.LINEAR
            }
            lineWidth = a.getFloat(R.styleable.LineChartView_lineWidth, lineWidth)
            circleRadius = a.getFloat(R.styleable.LineChartView_circlesRadius, circleRadius)
            showCircleHole = a.getBoolean(R.styleable.LineChartView_showCircleHole, showCircleHole)
            showDashedLine = a.getBoolean(R.styleable.LineChartView_showDashedLine, showDashedLine)
            showFilledArea = a.getBoolean(R.styleable.LineChartView_showFilledArea, showFilledArea)
            showCircle = a.getBoolean(R.styleable.LineChartView_showCircle, showCircle)
            chartRange = a.getFloat(R.styleable.LineChartView_range, chartRange)
            circleColor = a.getColor(R.styleable.LineChartView_circleColor, circleColor)
            gradientColor = a.getDrawable(R.styleable.LineChartView_gradientColor)
            lineColor = a.getColor(R.styleable.LineChartView_lineColor, lineColor)
            highlightColor = a.getColor(R.styleable.LineChartView_highlightColor, highlightColor)
        } finally {
            a.recycle()
        }

        setChartStyle()

        val chart = binding.chart
        chart.renderer = renderer
        chart.invalidate()

        addView(binding.root)
    }

    private fun setChartStyle() {
        val chart = binding.chart

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10f, 0f, 10f, 0f)

        chart.setDrawGridBackground(false)

        // disable description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)

        // set listeners
        chart.setOnChartValueSelectedListener(listener)

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        chart.legend.isEnabled = false

        chart.axisLeft.isEnabled = false
        chart.axisLeft.spaceTop = 40f
        chart.axisLeft.spaceBottom = 40f
        chart.axisRight.isEnabled = false
        chart.xAxis.isEnabled = false

        // create marker to display box when values are selected
        val mv = MyMarkerView(context, R.layout.custom_marker_view, "You reached your peak!")

        // Set the marker to the chart
        mv.chartView = chart
        chart.marker = mv

        // add nice and smooth animation
        // chart.animateY(1500)
    }

    fun clearChartData() {
        chartData.clear()
        chartDataEntries.clear()
        kotlin.runCatching { binding.chart.clearValues() }
        renderer.clearCirclePositions()
    }

    fun addChartData(value: Int) {
        renderer.clearCirclePositions()
        addChartData(listOf(value))
    }

    fun addChartData(values: List<Int>) {
        chartData.addAll(values)
        val maxChartData = chartData.maxOrNull()
        val labelPositions = ArrayList<Int>()
        chartData.forEachIndexed { index, lineChartData ->
            if (lineChartData == maxChartData) renderer.drawCircleAtPosition(index)
            if (lineChartData == maxChartData) labelPositions.add(index)
        }
        binding.chart.renderer = renderer
        kotlin.runCatching { binding.chart.clearValues() }
        values.forEach { drawLines(it) }

        // draw labels if needed
        drawLabels(labelPositions)
    }

    private fun drawLines(value: Int) {
        val chart = binding.chart

        val entryValue = (value * chartRange)
        //in this case the icon doesn't matter because is not shown
        chartDataEntries.add(Entry(chartDataEntries.size.toFloat(), entryValue, ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)))

        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = chartDataEntries
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(chartDataEntries, "DataSet 1")
            set1.setDrawIcons(false)
            set1.setDrawValues(false)
            set1.mode = lineMode
            set1.setDrawVerticalHighlightIndicator(false)
            set1.highLightColor = highlightColor

            // draw dashed line
            if (showDashedLine) {
                set1.enableDashedLine(10f, 5f, 0f)
            }

            // lines and points
            set1.color = lineColor
            set1.setCircleColor(circleColor)

            // line thickness and point size
            set1.lineWidth = lineWidth
            set1.circleRadius = circleRadius

            // draw points
            set1.setDrawCircles(showCircle)

            // draw points as solid circles
            set1.setDrawCircleHole(showCircleHole)

            // set the filled area
            set1.setDrawFilled(showFilledArea)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area
            set1.fillDrawable = gradientColor

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // set data
            chart.data = LineData(dataSets)
        }
        chart.invalidate()
    }

    private fun drawLabels(labelPositions: List<Int>) {
        if(labelPositions.isEmpty()) return
        binding.chart.highlightValue(null)
        for (i in binding.chart.data.dataSets.indices) {
            val dataSet = binding.chart.data.dataSets[i]
            labelPositions.forEach { pos ->
                val e = dataSet.getEntryForIndex(pos) ?: return@forEach
                val phaseY = renderer.chartAnimator.phaseY
                binding.chart.highlightValue(Highlight(e.x, e.y * phaseY, i))
            }
        }
    }
}