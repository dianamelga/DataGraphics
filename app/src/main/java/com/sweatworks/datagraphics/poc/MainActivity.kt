package com.sweatworks.datagraphics.poc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.sweatworks.datagraphics.poc.viewmodels.MainViewModel
import com.sweatworks.datagraphics.poc.viewmodels.NavigationState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.navigationState.observe(this) {
            when (it) {
                NavigationState.BarChartScreen -> goTo(BarChartFragment.newInstance(), BarChartFragment.TAG)
                NavigationState.LineChartScreen -> goTo(LineChartFragment.newInstance(), LineChartFragment.TAG)
                NavigationState.MainScreen -> goTo(MainFragment.newInstance(), MainFragment.TAG)
                NavigationState.BalanceAndForceScreen -> goTo(BalanceAndForceFragment.newInstance(), BalanceAndForceFragment.TAG)
            }
        }
    }

    private fun goTo(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            add(R.id.graphics_container,
                fragment,
                fragmentTag).addToBackStack(fragmentTag)
        }
    }
}