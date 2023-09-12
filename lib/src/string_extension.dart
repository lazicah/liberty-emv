import 'package:liberty_emv/src/liberty_emv.g.dart';

extension Stringify on TransactionDataResponse {
  String stringify() {
    return 'TransactionDataResponse(amount: $amount, deviceState: $deviceState, authorizationCode: $authorizationCode, cardExpiryDate: $cardExpiryDate, cardHolderName: $cardHolderName, date: $date, maskedPan: $maskedPan, merchantId: $merchantId, merchantName: $merchantName, responseCode: $responseCode, responseMessage: $responseMessage, rrn: $rrn, stan: $stan, terminalId: $terminalId, transactionType: $transactionType)';
  }
}

extension StringifyCard on CardDetails {
  String stringify() {
    return 'CardDetails{primaryAccountNumber: $primaryAccountNumber, track1: $track1, track2: $track2, expiryDate: $expiryDate, serviceCode: $serviceCode, iccCardData: $iccCardData, cardSequenceNumber: $cardSequenceNumber, pinBlock: $pinBlock, cardSlotTypeEnum: $cardSlotTypeEnum, cardHolderName: $cardHolderName}';
  }
}
