package service

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.libertyPay.horizonSDK.common.ActivityRequestAndResultCodes
import com.libertyPay.horizonSDK.common.PosTransactionException
import com.libertyPay.horizonSDK.common.TransactionIntentExtras
import com.libertypay.posclient.api.models.response.BalanceEnquiryResponseData
import io.flutter.plugins.Pigeon
import service.dto.PigeonResponseDto

class ActivityResultHandler(
    private val resultCallback: Pigeon.Result<Pigeon.EmvBalanceEnquiryResponse>?
) {

    private val functionMap = mapOf<Int, (data: Intent?, resultCode: Int) -> Boolean>(
        ActivityRequestAndResultCodes.TRANSACTION_SUCCESS_RESULT_CODE to ::handleBalanceEnquiryResponse,
        ActivityRequestAndResultCodes.TRANSACTION_FAILURE_RESULT_CODE to ::handleBalanceEnquiryFailure,
        ActivityRequestAndResultCodes.KEY_EXCHANGE_FAILED_RESULT_CODE to ::handleKeyExchangeFailure,
    )


    private fun handleBalanceEnquiryResponse(data: Intent?, resultCode: Int): Boolean {
        val balanceEnquiryData =
            data?.getParcelableExtra<BalanceEnquiryResponseData?>(TransactionIntentExtras.TRANSACTION_RESULT)


        balanceEnquiryData?.let {
            val emvBalanceResponse = PigeonResponseDto.toBalanceEnquiryResponse(it)
            resultCallback?.success(emvBalanceResponse)
        }
        return true
    }

    private fun handleBalanceEnquiryFailure(data: Intent?, resultCode: Int): Boolean {
        val posTransactionResponse =
            data?.getParcelableExtra<PosTransactionException>(TransactionIntentExtras.TRANSACTION_FAILURE)

        posTransactionResponse?.let {
            resultCallback?.error(Exception(it.errorMessage))
        }
        return true
    }

    private fun handleKeyExchangeFailure(data: Intent?, resultCode: Int): Boolean {
        val keyExchangeException =
            data?.getParcelableExtra<PosTransactionException>(TransactionIntentExtras.KEY_EXCHANGE_FAILURE)

        keyExchangeException?.let {
            resultCallback?.error(Exception(it.errorMessage))
        }
        return true
    }

    operator fun invoke(data: Intent?, resultCode: Int): Boolean {
        val handlerFunction = functionMap[resultCode]
        return handlerFunction?.invoke(data, resultCode) ?: false
    }

}