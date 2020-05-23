package com.kaiser.logica

import android.app.Application

class MyApplication: Application() {
    companion object {
        var globalVar = "publico"
        var usuario_id = ""
        var cadete_sin_cargo : Int = 0
        var token : String = ""
    }

    override fun onCreate() {
        super.onCreate()
        // initialization code here
    }
}