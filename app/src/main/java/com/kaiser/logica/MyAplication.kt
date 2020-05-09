package com.kaiser.logica

import android.app.Application

class MyApplication: Application() {
    companion object {
        var globalVar = "publico"
    }

    override fun onCreate() {
        super.onCreate()
        // initialization code here
    }
}