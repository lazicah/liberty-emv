package service.dto

import com.libertypay.posclient.api.models.response.BalanceEnquiryResponseData
import io.flutter.plugins.Pigeon


class PigeonResponseDto {
    
    companion object {

        fun toBalanceEnquiryResponse(emvBalanceResponse: BalanceEnquiryResponseData): Pigeon.EmvBalanceEnquiryResponse {
            return  Pigeon.EmvBalanceEnquiryResponse().apply {
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
    }
}