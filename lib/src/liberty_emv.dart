import 'dart:typed_data';

import 'package:liberty_emv/src/liberty_emv.g.dart';

class LibertyEmv {
  LibertyEmv._privateConstructor();

  static final LibertyEmv _instance = LibertyEmv._privateConstructor();

  static LibertyEmv get instance => _instance;

  final LibertyEmvApi _api = LibertyEmvApi();

  Future<TransactionDataResponse> initialise(Environment environment) async {
    return _api.initialise(environment);
  }

  Future<TransactionDataResponse> enquireBalance(
    bool isOfflineTransaction,
    AccountType accountType,
    String rrn,
  ) async {
    return _api.enquireBalance(isOfflineTransaction, accountType, rrn);
  }

  Future<TransactionDataResponse> purchase(
    double amount,
    AccountType accountType,
    String rrn,
  ) async {
    return _api.purchase(amount, accountType, rrn);
  }

  Future<TransactionDataResponse> performKeyExchange() async {
    return _api.performKeyExchange();
  }

  Future<TransactionDataResponse> print(Uint8List bitmap) async {
    return _api.print(bitmap);
  }

  Future<CardDetails?> getCardDetails() async {
    return _api.getCardDetails();
  }

  Future<String?> getSerialNo() async {
    return _api.getSerialNo();
  }

  void launchAppStore() {
    _api.launchAppStore();
  }
}
