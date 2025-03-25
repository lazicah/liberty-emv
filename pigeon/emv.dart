import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  input: 'pigeon/emv.dart',
  dartOut: 'lib/src/liberty_emv.g.dart',
  javaOut: 'android/src/main/java/io/flutter/plugins/LibertyEmv.java',
))
enum AccountType {
  defaultUnspecified,
  savings,
  current,
  credit,
  universal,
  investment
}

enum Environment { live, test }

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

  final bool? isSuccessful;

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
    this.isSuccessful,
  );
}

class CardDetails {
  final String? primaryAccountNumber;

  final String? track1;

  final String? track2;

  final String? expiryDate;

  final String? serviceCode;

  final String? iccCardData;

  final String? cardSequenceNumber;

  final String? pinBlock;

  final String? cardSlotTypeEnum;

  final String? cardHolderName;

  CardDetails(
    this.primaryAccountNumber,
    this.track1,
    this.track2,
    this.expiryDate,
    this.serviceCode,
    this.iccCardData,
    this.cardSequenceNumber,
    this.pinBlock,
    this.cardSlotTypeEnum,
    this.cardHolderName,
  );
}

@HostApi()
abstract class LibertyEmvApi {
  @async
  TransactionDataResponse initialise(Environment environment);

  @async
  TransactionDataResponse enquireBalance(
    bool isOfflineTransaction,
    AccountType accountType,
    String rrn,
  );

  @async
  TransactionDataResponse purchase(
    double amount,
    AccountType accountType,
    String rrn,
  );

  @async
  TransactionDataResponse performKeyExchange();

  @async
  TransactionDataResponse print(Uint8List bitmap);

  @async
  CardDetails? getCardDetails();

  @async
  String? getSerialNo();

  void launchAppStore();
}
