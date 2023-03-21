package com.liberty.emv.liberty_emv

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.libertyPay.horizonSDK.LibertyHorizonSDK
import com.libertyPay.horizonSDK.Preferences
import com.libertypay.posclient.PosRemoteClient
import com.libertypay.posclient.api.Environment

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.Pigeon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import service.EmvService
import timber.log.Timber

/** LibertyEmvPlugin */
class LibertyEmvPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {


    private lateinit var emvService: EmvService

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        emvService = EmvService(flutterPluginBinding.applicationContext)
        Pigeon.EmvApi.setup(flutterPluginBinding.binaryMessenger, emvService)
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }


    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        emvService.initialize(binding)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        PosRemoteClient.initPosApi()
        Preferences.init(binding.activity)
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
        emvService.activityBinding = null
    }


    override fun onMethodCall(call: MethodCall, result: Result) {
    }

}

