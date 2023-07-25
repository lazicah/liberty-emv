package service

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import com.liberty.emv.liberty_emv.Constants
import com.liberty.emv.liberty_emv.DeviceState
import com.libertyPay.horizonSDK.LibertyHorizonSDK
import com.libertyPay.horizonSDK.domain.models.AccountType
import com.libertyPay.horizonSDK.domain.models.RetrievalReferenceNumber
import com.libertyPay.horizonSDK.domain.models.TransactionAmount
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.LibertyEmv
import io.flutter.plugins.LibertyEmv.FlutterError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class EmvService(private val context: Context) : LibertyEmv.LibertyEmvApi,
    PluginRegistry.ActivityResultListener {

    var activityBinding: ActivityPluginBinding? = null
    private var resultCallback: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>? = null
    var isSdkInitialised: Boolean = false

    fun initialize(binding: ActivityPluginBinding) {
        activityBinding = binding
        binding.addActivityResultListener(this)
    }

    override fun enquireBalance(isOfflineTransaction: Boolean, accountType: LibertyEmv.AccountType, rrn: String, result: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyHorizonSDK.initialize(activityBinding!!.activity)
            resultCallback = result
            val accountTypeEnum =
                Constants.transactionTypeMap[accountType] ?: AccountType.DEFAULT_UNSPECIFIED
            if(isSdkInitialised) {
                activityBinding?.activity?.let {
                    LibertyHorizonSDK.startBalanceEnquiryDialogActivity(
                            activity = it,
                            accountType = accountTypeEnum,
                            isOfflineTransaction = isOfflineTransaction,
                            retrievalReferenceNumber = RetrievalReferenceNumber(rrn)
                    )
                }
            } else {
                Timber.tag(TAG).d("Have you called [initialise]?")
            }
        }

    }

    override fun initialise(environment: LibertyEmv.Environment, result: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {


            Timber.tag(TAG).d("initialize: sdk initializing")
            try {
                LibertyHorizonSDK.initialize(activityBinding!!.activity)
                isSdkInitialised = true

                val keyExchangeResponse = LibertyEmv.TransactionDataResponse().apply {
                    deviceState = DeviceState.SUCCESSFUL.value
                    responseMessage = "Initialised"
                }
                result.success(keyExchangeResponse)
            } catch (e: Exception) {
                Timber.tag(TAG).d("initialize error: %s", e.message)
            }


    }

    override fun purchase(amount: String, accountType: LibertyEmv.AccountType, rrn: String, result: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyHorizonSDK.initialize(activityBinding!!.activity)
            resultCallback = result
            val accountTypeEnum =
                Constants.transactionTypeMap[accountType] ?: AccountType.DEFAULT_UNSPECIFIED

            if(isSdkInitialised) {
                activityBinding?.activity?.let {
                    LibertyHorizonSDK.startPurchaseActivity(
                            activity = it,
                            transactionAmount = TransactionAmount(amount),
                            accountType = accountTypeEnum,
                            retrievalReferenceNumber = RetrievalReferenceNumber(rrn)
                    )
                }
            }else {
                Timber.tag(TAG).d("Have you called [initialise]?")
            }
        }

    }


    override fun performKeyExchange(result: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyHorizonSDK.initialize(activityBinding!!.activity)
            if(isSdkInitialised) {
                val keyExchangeSuccess: Boolean = LibertyHorizonSDK.doKeyExchange()

                if (keyExchangeSuccess) {
                    //Handle Success
                    val keyExchangeResponse = LibertyEmv.TransactionDataResponse().apply {
                        deviceState = DeviceState.SUCCESSFUL.value
                        isSuccessful = true
                        responseMessage = "Key exchange successful"
                    }
                    result.success(keyExchangeResponse)
                } else {
                    // Handle failure
                    val keyExchangeResponse = LibertyEmv.TransactionDataResponse().apply {
                        deviceState = DeviceState.ERROR.value
                        isSuccessful = false
                        responseMessage = "Key exchange service currently unavailable"
                    }
                    result.success(keyExchangeResponse)
                }
            }else {
                Timber.tag(TAG).d("Have you called [initialise]?")
            }

        }
    }

    override fun print(bitmap: ByteArray, result:LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            resultCallback = result
            val value = BitmapFactory.decodeByteArray(bitmap,0,bitmap.size)
            LibertyHorizonSDK.print(value)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Timber.tag(TAG).d("onActivityResult: %s", resultCode)
        Timber.tag(TAG).d("onActivityResult:changed")
        val handler = ActivityResultHandler(resultCallback)
        return handler(data, resultCode, requestCode)
    }



}


