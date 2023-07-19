import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  input: 'pigeon/emv.dart',
  dartOut: 'lib/src/liberty_emv.g.dart',
  javaOut: 'android/src/main/java/io/flutter/plugins/LibertyEmv.java',
))

enum AccountType{defaultUnspecified,savings,current,credit,universal,investment}
enum Environment{live,test}

class KeyExchangeResponse {
  final String? deviceState;
  final bool? isSuccessful;
  final Map<String?, String?>? responseData;

  KeyExchangeResponse(this.deviceState, this.isSuccessful, this.responseData);
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
abstract class LibertyEmvApi {
  @async
  void initialise(Environment environment);
  @async
  TransactionDataResponse? enquireBalance(
    bool isOfflineTransaction, AccountType accountType,
    String rrn,
  );
  @async
  TransactionDataResponse? purchase(
    String amount,
    AccountType accountType,
    String rrn,
  );
  @async
  KeyExchangeResponse? performKeyExchange();
  @async
  void print(Uint8List bitmap);
}
