package com.liberty.emv.liberty_emv

import android.app.Application
import com.libertyPay.horizonSDK.LibertyHorizonSDK

class Application : Application() {


    override fun onCreate() {
        super.onCreate()

        LibertyHorizonSDK.initialize(this)
    }
}