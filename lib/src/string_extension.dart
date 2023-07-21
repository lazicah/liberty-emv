import 'package:liberty_emv/src/liberty_emv.g.dart';

extension Stringify on TransactionDataResponse {
  String stringify() {
    return 'TransactionDataResponse(amount: $amount, deviceState: $deviceState, authorizationCode: $authorizationCode, cardExpiryDate: $cardExpiryDate, cardHolderName: $cardHolderName, date: $date, maskedPan: $maskedPan, merchantId: $merchantId, merchantName: $merchantName, responseCode: $responseCode, responseMessage: $responseMessage, rrn: $rrn, stan: $stan, terminalId: $terminalId, transactionType: $transactionType)';
  }
}

