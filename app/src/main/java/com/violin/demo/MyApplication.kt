package com.violin.demo

import android.app.Application

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(AUncaughtExceptionHandler(this))
    }
}