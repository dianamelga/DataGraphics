package com.sweatworks.datagraphics.poc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sweatworks.datagraphics.poc.databinding.FragmentMainBinding
import com.sweatworks.datagraphics.poc.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val rootViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.barChartBtn.setOnClickListener { rootViewModel.goToBarChartScreen() }
        binding.lineChartBtn.setOnClickListener { rootViewModel.goToLineChartScreen() }
    }

    companion object {

        val TAG = MainFragment::javaClass.name
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}