package service

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.liberty.emv.liberty_emv.Constants
import com.libertyPay.horizonSDK.LibertyHorizonSDK
import com.libertyPay.horizonSDK.common.ActivityRequestAndResultCodes
import com.libertyPay.horizonSDK.domain.models.AccountType
import com.libertyPay.horizonSDK.domain.models.TransactionAmount
import com.libertypay.posclient.api.Environment
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.Pigeon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmvService(private val context: Context) : Pigeon.EmvApi,
    PluginRegistry.ActivityResultListener {


    var activityBinding: ActivityPluginBinding? = null
    private var resultCallback: Pigeon.Result<Pigeon.TransactionDataResponse>? = null

    fun initialize(binding: ActivityPluginBinding) {
        activityBinding = binding
        binding.addActivityResultListener(this)
    }

    override fun enquireBalance(
        tID: String,
        accountType: String,
        result: Pigeon.Result<Pigeon.TransactionDataResponse>?
    ) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyHorizonSDK.initialize(activityBinding!!.activity, environment = Environment.Live)

            delay(1000)
            resultCallback = result
            val accountTypeEnum =
                Constants.transactionTypeMap[accountType] ?: AccountType.DEFAULT_UNSPECIFIED

            activityBinding?.activity?.let {
                LibertyHorizonSDK.startBalanceEnquiryDialogActivity(
                    activity = it,
                    accountType = accountTypeEnum,
                    terminalId = tID,
                )
            }
        }

    }

    override fun purchase(
        amount: String,
        accountType: String,
        result: Pigeon.Result<Pigeon.TransactionDataResponse>?
    ) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyHorizonSDK.initialize(activityBinding!!.activity, environment = Environment.Live)

            delay(1000)
            resultCallback = result
            val accountTypeEnum =
                Constants.transactionTypeMap[accountType] ?: AccountType.DEFAULT_UNSPECIFIED

            activityBinding?.activity?.let {
                LibertyHorizonSDK.startPurchaseActivity(
                    activity = it,
                    transactionAmount = TransactionAmount(amount),
                    accountType = accountTypeEnum
                )
            }
        }

    }


    override fun performKeyExchange(result: Pigeon.Result<Boolean>?) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val response = LibertyHorizonSDK.doKeyExchange()
            result?.success(response)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d(TAG, "onActivityResult: $resultCode")
        Log.d(TAG, "onActivityResult:changed")
        val handler = ActivityResultHandler(resultCallback)
        return handler(data, resultCode, requestCode)
    }


}