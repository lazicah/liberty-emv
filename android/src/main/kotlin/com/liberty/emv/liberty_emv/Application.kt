package com.liberty.emv.liberty_emv

import android.app.Application


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
//        LibertyHorizonSDK.initialize(this, environment = Environment.Live)
    }
}