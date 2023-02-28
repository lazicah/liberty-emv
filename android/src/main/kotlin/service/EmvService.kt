package service

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.libertyPay.horizonSDK.LibertyHorizonSDK
import com.libertyPay.horizonSDK.common.ActivityRequestAndResultCodes
import com.libertyPay.horizonSDK.common.PosTransactionException
import com.libertypay.posclient.api.models.response.BalanceEnquiryResponseData
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugins.Pigeon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import service.dto.PigeonResponseDto
import kotlin.Exception

class EmvService(private val context: Context) : Pigeon.EmvApi {

    var activityBinding: ActivityPluginBinding? = null
    private var resultCallback: Pigeon.Result<Pigeon.EmvBalanceEnquiryResponse>? = null


    override fun enquireBalance(
        tID: String,
        result: Pigeon.Result<Pigeon.EmvBalanceEnquiryResponse>?
    ) {
        resultCallback = result

        activityBinding?.activity?.let {
            LibertyHorizonSDK.startBalanceEnquiryDialogActivity(
                tID,
                activity = it
            )
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
        if (data?.hasExtra("transactionResult") == true) {
            return handleBalanceEnquiryResponse(data, resultCode)
        }
        Log.d(TAG, "onActivityResult: $resultCode")
        Log.d(ContentValues.TAG, "onActivityResult: $data")
        return false
    }

    private fun handleBalanceEnquiryResponse(data: Intent?, resultCode: Int): Boolean {
        if (resultCode == ActivityRequestAndResultCodes.TRANSACTION_SUCCESS_REQUEST_CODE) {
            val balanceEnquiryData =
                data?.getParcelableExtra<BalanceEnquiryResponseData?>("transactionResult")

            balanceEnquiryData?.let {
                val emvBalanceResponse =
                    PigeonResponseDto.toBalanceEnquiryResponse(it)
                resultCallback?.success(emvBalanceResponse)
            }
        }


        if (resultCode == ActivityRequestAndResultCodes.TRANSACTION_FAILURE_REQUEST_CODE) {
            val posTransactionResponse =
                data?.getParcelableExtra<PosTransactionException>("transactionFailure")

            posTransactionResponse?.let {
                resultCallback?.error(Exception(it.errorMessage))
            }
        }

        return true
    }

}