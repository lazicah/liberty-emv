package service

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

interface EmvService {
    fun initializeService(binding: ActivityPluginBinding)

    fun closeService()
}