package service

import android.content.Intent
import com.liberty.emv.liberty_emv.DeviceState
import com.libertyPay.posSdk.common.ActivityRequestAndResultCodes
import com.libertyPay.posSdk.common.TransactionIntentExtras
import com.libertyPay.posSdk.data.remote.models.response.TransactionData
import io.flutter.plugins.LibertyEmv
import io.flutter.plugins.LibertyEmv.FlutterError
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
        val errorMessage = data?.getStringExtra("error_message")
        if (transactionFailureDetails != null) {
            transactionFailureDetails.let {
                val emvResponse = PigeonResponseDto.toTransactionData(it)
                emvResponse.deviceState = DeviceState.TRANS_FAILED.value
                resultCallback?.success(emvResponse)
            }
        } else {
            resultCallback?.error(FlutterError(resultCode.toString(), errorMessage, null))
        }

        return true
    }

    private fun handleKeyExchangeFailure(
        data: Intent?,
        resultCode: Int,
        requestCode: Int
    ): Boolean {
        val errorMessage = data?.getStringExtra("error_message")
        resultCallback?.error(FlutterError(resultCode.toString(), errorMessage, null))
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
        println("Activity Result Code: $resultCode")
        val handlerFunction = functionMap[resultCode]
        return handlerFunction?.invoke(data, resultCode, requestCode) ?: false
    }

}