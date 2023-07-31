package com.liberty.emv.liberty_emv

import android.content.ContentValues.TAG
import android.os.Build
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
import service.HorizonEmvService
import service.NexgoEmvService
import timber.log.Timber

/** LibertyEmvPlugin */
class LibertyEmvPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {
    private lateinit var emvService: EmvService

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        var model: String = Build.MODEL
        Timber.tag(TAG).d("Device Model: $model")
        if (model.uppercase() == "N86") {
            emvService = NexgoEmvService(flutterPluginBinding.applicationContext)
            LibertyEmv.LibertyEmvApi.setup(flutterPluginBinding.binaryMessenger, emvService as NexgoEmvService)
        } else {
            emvService = HorizonEmvService(flutterPluginBinding.applicationContext)
            LibertyEmv.LibertyEmvApi.setup(flutterPluginBinding.binaryMessenger, emvService as HorizonEmvService)
        }
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

