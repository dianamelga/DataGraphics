package com.sweatworks.datagraphics.poc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.LineDataSet
import com.sweatworks.datagraphics.poc.databinding.FragmentLineChartBinding
import java.util.Timer
import java.util.TimerTask

class LineChartFragment : Fragment() {

    private lateinit var binding: FragmentLineChartBinding

    private var chartData = ArrayList<Int>()

    private var chartTask = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLineChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawGraphic()

        binding.redrawBtn.setOnClickListener {
            drawGraphic()
        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            binding.lineChartView.lineMode = when {
                binding.rbStepped.isChecked -> LineDataSet.Mode.STEPPED
                binding.rbCubic.isChecked -> LineDataSet.Mode.CUBIC_BEZIER
                binding.rbHorizontal.isChecked -> LineDataSet.Mode.HORIZONTAL_BEZIER
                else -> LineDataSet.Mode.LINEAR
            }
            drawGraphic(false)
        }

        binding.swFillArea.setOnCheckedChangeListener { compoundButton, b ->
            binding.lineChartView.showFilledArea = b
            drawGraphic(false)
        }
    }

    private fun drawGraphic(regenerate: Boolean = true) {
        binding.lineChartView.clearChartData()
        if (regenerate) {
            chartData.clear()
            chartTask.cancel()
            chartTask.purge()
            chartTask = Timer()
            chartTask.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val data = ((0..30).random())
                    chartData.add(data)
                    binding.lineChartView.apply {
                        addChartData(data)
                    }
                }
            }, 0, 1000)


            // for (i in 0..10) {
            //     val data = (i * Math.random()).toInt() - 30
            //     chartData.add(data)
            // }
            // binding.lineChartView.apply {
            //     addChartData(chartData.mapIndexed { idx, data -> LineChartData(data,
            //         chartData.maxOrNull() == data,
            //         chartData.maxOrNull() == data) })
            // }
        } else {
            binding.lineChartView.addChartData(chartData)
        }
    }

    companion object {
        val TAG = LineChartFragment::javaClass.name

        @JvmStatic
        fun newInstance() = LineChartFragment()
    }
}