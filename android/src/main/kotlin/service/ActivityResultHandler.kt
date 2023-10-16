package service

import android.content.Intent
import com.liberty.emv.liberty_emv.DeviceState
import com.libertyPay.posSdk.common.ActivityRequestAndResultCodes
import com.libertyPay.posSdk.common.TransactionIntentExtras
import com.libertyPay.posSdk.data.remote.models.response.TransactionData
import com.libertyPay.posSdk.domain.models.PosTransactionException
import io.flutter.plugins.LibertyEmv
import service.dto.PigeonResponseDto

class ActivityResultHandler(
    private val resultCallback: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>?
) {

    private val functionMap =
        mapOf<Int, (data: Intent?, resultCode: Int, requestCode: Int) -> Boolean>(
            ActivityRequestAndResultCodes.TRANSACTION_SUCCESS_RESULT_CODE to ::handleSuccessResponse,
            ActivityRequestAndResultCodes.TRANSACTION_FAILURE_RESULT_CODE to ::handleTransactionFailure,
            ActivityRequestAndResultCodes.KEY_EXCHANGE_FAILED_RESULT_CODE to ::handleKeyExchangeFailure,
            0 to ::handleCancelledTransaction,
        )


    private fun handleSuccessResponse(data: Intent?, resultCode: Int, requestCode: Int): Boolean {
        val transactionSuccessDetails =
                data?.getParcelableExtra<TransactionData?>(TransactionIntentExtras.TRANSACTION_RESULT)

        transactionSuccessDetails?.let {
            val emvResponse = PigeonResponseDto.toTransactionData(it)
            emvResponse.deviceState = DeviceState.TRANS_DONE.value
            resultCallback?.success(emvResponse)
        }
        return true
    }


    private fun handleTransactionFailure(
        data: Intent?,
        resultCode: Int,
        requestCode: Int
    ): Boolean {
        val transactionFailureDetails =
                data?.getParcelableExtra<TransactionData?>(TransactionIntentExtras.TRANSACTION_RESULT)

        transactionFailureDetails?.let {
            val emvResponse = PigeonResponseDto.toTransactionData(it)
            emvResponse.deviceState = DeviceState.TRANS_FAILED.value
            resultCallback?.success(emvResponse)
        }
        return true
    }

    private fun handleKeyExchangeFailure(
        data: Intent?,
        resultCode: Int,
        requestCode: Int
    ): Boolean {
        val keyExchangeException =
            data?.getParcelableExtra<PosTransactionException>(TransactionIntentExtras.KEY_EXCHANGE_FAILURE)

        keyExchangeException?.let {
            resultCallback?.error(Exception(it.errorMessage))
        }
        return true
    }

    private fun handleCancelledTransaction(
        data: Intent?,
        resultCode: Int,
        requestCode: Int
    ): Boolean {
        val response = LibertyEmv.TransactionDataResponse().apply {
            deviceState = DeviceState.TRANS_CANCELLED.value
        }
        resultCallback?.success(response)
        return true
    }

    operator fun invoke(data: Intent?, resultCode: Int, requestCode: Int): Boolean {
        val handlerFunction = functionMap[resultCode]
        return handlerFunction?.invoke(data, resultCode, requestCode) ?: false
    }

}