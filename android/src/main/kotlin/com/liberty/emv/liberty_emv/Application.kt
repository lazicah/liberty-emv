package com.liberty.emv.liberty_emv

import android.app.Application
import com.libertyPay.horizonSDK.LibertyHorizonSDK
import com.libertypay.posclient.api.Environment


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        LibertyHorizonSDK.initialize(this, environment = Environment.Live)
    }
}