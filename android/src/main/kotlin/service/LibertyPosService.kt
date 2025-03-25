package service

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import com.liberty.emv.liberty_emv.Constants
import com.liberty.emv.liberty_emv.DeviceState
import com.libertyPay.posSdk.LibertyPosSdk
import com.libertyPay.posSdk.domain.models.AccountType
import com.libertyPay.posSdk.domain.models.Card
import com.libertyPay.posSdk.domain.models.RetrievalReferenceNumber
import com.libertyPay.posSdk.domain.models.TransactionAmount
import com.nexgo.external.utils.DeviceHelper
import com.nexgo.oaf.apiv3.DeviceEngine
import com.nexgo.oaf.apiv3.DeviceInfo
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.LibertyEmv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import service.dto.PigeonResponseDto
import timber.log.Timber

class LibertyPosService(private val context: Context) : LibertyEmv.LibertyEmvApi,
    PluginRegistry.ActivityResultListener, EmvService {

    var activityBinding: ActivityPluginBinding? = null
    private var resultCallback: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>? = null
    var isSdkInitialised: Boolean = false

    override fun initializeService(binding: ActivityPluginBinding) {
        activityBinding = binding
        binding.addActivityResultListener(this)
    }

    override fun closeService() {
        activityBinding = null;
    }

    override fun enquireBalance(isOfflineTransaction: Boolean, accountType: LibertyEmv.AccountType, rrn: String, result: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {

            LibertyPosSdk.initialize(activityBinding!!.activity)
            resultCallback = result
            val accountTypeEnum =
                Constants.accountTypeMapHorizon[accountType] ?: AccountType.DEFAULT_UNSPECIFIED
            if(isSdkInitialised) {
                activityBinding?.activity?.let {
                    LibertyPosSdk.startBalanceEnquiryDialogActivity(
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
                LibertyPosSdk.initialize(activityBinding!!.activity)
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

    override fun purchase(amount: Double, accountType: LibertyEmv.AccountType, rrn: String, result: LibertyEmv.Result<LibertyEmv.TransactionDataResponse>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyPosSdk.initialize(activityBinding!!.activity)
            resultCallback = result
            val accountTypeEnum =
                Constants.accountTypeMapHorizon[accountType] ?: AccountType.DEFAULT_UNSPECIFIED

            if(isSdkInitialised) {
                activityBinding?.activity?.let {
                    LibertyPosSdk.startPurchaseActivity(
                            activity = it,
                            transactionAmount = TransactionAmount(amount.toBigDecimal()),
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
            LibertyPosSdk.initialize(activityBinding!!.activity)
            if(isSdkInitialised) {
                val keyExchangeSuccess: Boolean = LibertyPosSdk.doKeyExchange()

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
            try {
                val value = BitmapFactory.decodeByteArray(bitmap,0,bitmap.size)
                LibertyPosSdk.printReceipt(value)
                val printResponse = LibertyEmv.TransactionDataResponse().apply {
                    deviceState = DeviceState.PRINTING_DONE.value
                    isSuccessful = true
                    responseMessage = "Printed"
                }
                result?.success(printResponse)
            } catch (e : Exception){
                Timber.tag(TAG).e("Printing Error: ${e.message}")
                val printResponse = LibertyEmv.TransactionDataResponse().apply {
                    deviceState = DeviceState.PRINTING_FAILED.value
                    isSuccessful = false
                    responseMessage = "${e.message}"
                }
                result?.success(printResponse)
            }
        }
    }

    override fun getCardDetails(result: LibertyEmv.Result<LibertyEmv.CardDetails>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyPosSdk.initialize(activityBinding!!.activity)
            val card: Card? = LibertyPosSdk.getCardDetails()
            card?.let {
                val cardDetails = PigeonResponseDto.toCardDetailsData(it)
                result.success(cardDetails)
            }
        }
    }

    override fun getSerialNo(result: LibertyEmv.Result<String>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            LibertyPosSdk.initialize(activityBinding!!.activity)
            val serial = LibertyPosSdk.getDeviceSerialNumber()
            serial?.let {
                result.success(serial)
            }
        }
    }

    override fun launchAppStore() {
        LibertyPosSdk.launchAppStore(context);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Timber.tag(TAG).d("onActivityResult: %s", resultCode)
        Timber.tag(TAG).d("onActivityResult:changed")
        val handler = ActivityResultHandler(resultCallback)
        return handler(data, resultCode, requestCode)
    }
}


