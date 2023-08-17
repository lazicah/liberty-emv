package com.liberty.emv.liberty_emv

import androidx.annotation.NonNull
import androidx.viewbinding.BuildConfig
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugins.LibertyEmv
import service.EmvService
import service.LibertyPosService
import timber.log.Timber

/** LibertyEmvPlugin */
class LibertyEmvPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {
    private lateinit var emvService: EmvService

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
            emvService = LibertyPosService(flutterPluginBinding.applicationContext)
            LibertyEmv.LibertyEmvApi.setup(flutterPluginBinding.binaryMessenger, emvService as LibertyPosService)
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }


    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        emvService.initializeService(binding)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
        emvService.closeService()
    }


    override fun onMethodCall(call: MethodCall, result: Result) {
    }

}

