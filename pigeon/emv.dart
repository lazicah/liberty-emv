import 'package:pigeon/pigeon.dart';

class EmvBalanceEnquiryResponse {
  final String? amount;

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

  EmvBalanceEnquiryResponse(
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
  );
}

@HostApi()
abstract class EmvApi {
  @async
  EmvBalanceEnquiryResponse enquireBalance(String tID);
  @async
  bool performKeyExchange();
}
