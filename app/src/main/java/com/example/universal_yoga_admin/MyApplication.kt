package com.example.universal_yoga_admin

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }
}