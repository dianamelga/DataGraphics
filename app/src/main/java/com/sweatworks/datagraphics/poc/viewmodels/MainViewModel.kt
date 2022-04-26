package com.sweatworks.datagraphics.poc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _navigationState = MutableLiveData<NavigationState>(NavigationState.MainScreen)
    val navigationState: LiveData<NavigationState> = _navigationState

    fun goToBarChartScreen() {
        navigateTo(NavigationState.BarChartScreen)
    }

    fun goToLineChartScreen() {
        navigateTo(NavigationState.LineChartScreen)
    }

    private fun navigateTo(screen: NavigationState) {
        _navigationState.postValue(screen)
    }
}

sealed class NavigationState {
    object MainScreen : NavigationState()
    object BarChartScreen : NavigationState()
    object LineChartScreen : NavigationState()
}