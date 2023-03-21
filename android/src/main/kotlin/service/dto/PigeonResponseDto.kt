package service.dto

import com.libertypay.posclient.api.models.response.BalanceEnquiryResponseData
import com.libertypay.posclient.api.models.response.TransactionData
import io.flutter.plugins.Pigeon


class PigeonResponseDto {
    
    companion object {

        fun toBalanceEnquiryResponse(emvBalanceResponse: BalanceEnquiryResponseData): Pigeon.TransactionDataResponse {
            return  Pigeon.TransactionDataResponse().apply {
                amount = emvBalanceResponse.amount
                authorizationCode = emvBalanceResponse.authorizationCode
                cardExpiryDate = emvBalanceResponse.cardExpiryDate
                cardHolderName = emvBalanceResponse.cardHolderName
                date = emvBalanceResponse.date
                maskedPan = emvBalanceResponse.maskedPan
                merchantId = emvBalanceResponse.merchantId
                merchantName = emvBalanceResponse.merchantName
                responseCode = emvBalanceResponse.responseCode
                responseMessage = emvBalanceResponse.responseMessage
                rrn = emvBalanceResponse.rrn
                stan = emvBalanceResponse.stan
                terminalId = emvBalanceResponse.terminalId
                transactionType = emvBalanceResponse.transactionType

            }
        }

        fun toPurchaseResponse(transactionResponse: TransactionData): Pigeon.TransactionDataResponse {
            return  Pigeon.TransactionDataResponse().apply {
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