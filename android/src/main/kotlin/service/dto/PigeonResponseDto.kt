package service.dto

import com.libertyPay.posSdk.data.remote.models.response.BalanceEnquiryResponseData
import com.libertyPay.posSdk.data.remote.models.response.TransactionData
import com.libertyPay.posSdk.domain.models.Card
import io.flutter.plugins.LibertyEmv


class PigeonResponseDto {
    
    companion object {

        fun toCardDetailsData(cardResponse: Card) : LibertyEmv.CardDetails{
            return LibertyEmv.CardDetails().apply {
                primaryAccountNumber = cardResponse.primaryAccountNumber
                track1 = cardResponse.track1
                track2 = cardResponse.track2
                expiryDate = cardResponse.expiryDate
                serviceCode = cardResponse.serviceCode
                iccCardData = cardResponse.iccCardData
                cardSequenceNumber = cardResponse.cardSequenceNumber
                pinBlock = cardResponse.pinBlock
                cardSlotTypeEnum = cardResponse.cardSlotTypeEnum?.name
                cardHolderName = cardResponse.cardHolderName
            }
        }

        fun toTransactionData(transactionResponse: TransactionData): LibertyEmv.TransactionDataResponse {
            return  LibertyEmv.TransactionDataResponse().apply {
                amount = transactionResponse.amount
                authorizationCode = transactionResponse.authorizationCode
                cardExpiryDate = transactionResponse.cardExpiryDate
                cardHolderName = transactionResponse.cardHolderName
                date = transactionResponse.date
                maskedPan = transactionResponse.maskedPan
                merchantId = transactionResponse.merchantId
                merchantName = transactionResponse.merchantName
                responseCode = transactionResponse.responseCode
                responseMessage = transactionResponse.responseMessage
                rrn = transactionResponse.rrn
                stan = transactionResponse.stan
                terminalId = transactionResponse.terminalId
                transactionType = transactionResponse.transactionType

            }
        }
    }
}