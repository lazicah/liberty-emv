package service

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.liberty.emv.liberty_emv.Constants
import com.libertyPay.horizonSDK.LibertyHorizonSDK
import com.libertyPay.horizonSDK.common.AccountTypes
import com.libertyPay.horizonSDK.common.ActivityRequestAndResultCodes
import com.libertyPay.horizonSDK.common.PosTransactionException
import com.libertyPay.horizonSDK.domain.AccountType
import com.libertypay.posclient.api.models.response.BalanceEnquiryResponseData
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugins.Pigeon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import service.dto.PigeonResponseDto
import timber.log.Timber
import kotlin.Exception

class EmvService(private val context: Context) : Pigeon.EmvApi {

    var activityBinding: ActivityPluginBinding? = null
    private var resultCallback: Pigeon.Result<Pigeon.EmvBalanceEnquiryResponse>? = null
    override fun enquireBalance(
        tID: String,
        accountType: String,
        result: Pigeon.Result<Pigeon.EmvBalanceEnquiryResponse>?
    ) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyHorizonSDK.initialize(activityBinding!!.activity)

            delay(1000)
            resultCallback = result
            val accountTypeEnum =
                Constants.transactionTypeMap[accountType] ?: AccountTypes.DEFAULT_UNSPECIFIED

            activityBinding?.activity?.let {
                LibertyHorizonSDK.startBalanceEnquiryDialogActivity(
                    activity = it,
                    accountType = accountTypeEnum,
                    terminalId = tID,
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


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val handler = ActivityResultHandler(resultCallback)
        data?.let { return handler(it, resultCode) }
        return false
    }


}