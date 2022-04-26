package com.sweatworks.datagraphics.poc.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DataGraphicsApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}