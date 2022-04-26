package com.sweatworks.datagraphics.poc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sweatworks.datagraphics.poc.databinding.FragmentBarChartBinding

class BarChartFragment : Fragment() {

    private lateinit var binding: FragmentBarChartBinding
    private var chartData = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBarChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawGraphic()

    }

    private fun drawGraphic(regenerate: Boolean = true) {
        binding.chart.clearChartData()
        if (regenerate) {
            chartData.clear()
            for (i in 0..5) {
                val data = (i * Math.random()).toInt()
                chartData.add(data)
                binding.chart.addChartData(data)
            }
        } else {
            chartData.forEach {
                binding.chart.addChartData(it)
            }
        }
    }

    companion object {
        val TAG = BarChartFragment::javaClass.name
        @JvmStatic
        fun newInstance() = BarChartFragment()
    }
}