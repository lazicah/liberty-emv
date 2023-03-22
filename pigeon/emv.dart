import 'package:pigeon/pigeon.dart';

class KeyExchangeResponse {
  final String? deviceState;
  final bool? isSuccessful;

  KeyExchangeResponse(this.deviceState, this.isSuccessful);
}

class TransactionDataResponse {
  final String? amount;

  final String? deviceState;

  final String? authorizationCode;

  final String? cardExpiryDate;

  final String? cardHolderName;

  final String? date;

  final String? maskedPan;

  final String? merchantId;

  final String? merchantName;

  final String? responseCode;

  final String? responseMessage;

  final String? rrn;

  final String? stan;

  final String? terminalId;

  final String? transactionType;

  TransactionDataResponse(
    this.amount,
    this.authorizationCode,
    this.cardExpiryDate,
    this.cardHolderName,
    this.date,
    this.maskedPan,
    this.merchantId,
    this.merchantName,
    this.responseCode,
    this.responseMessage,
    this.rrn,
    this.stan,
    this.terminalId,
    this.transactionType,
    this.deviceState,
  );
}

@HostApi()
abstract class EmvApi {
  @async
  TransactionDataResponse enquireBalance(String tID, String accountType);
  @async
  TransactionDataResponse purchase(String amount, String accountType);
  @async
  KeyExchangeResponse performKeyExchange();
}
