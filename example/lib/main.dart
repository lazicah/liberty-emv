import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:liberty_emv/liberty_emv.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    LibertyEmv.instance.initialise(Environment.live);
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> enquireBalance() async {
    final res = await LibertyEmv.instance
        .enquireBalance(false, AccountType.savings, "123456789132");

    print(res);
    print(res?.stringify());
  }

  Future<void> withdrawal() async {
    final res = await LibertyEmv.instance
        .purchase(100, AccountType.savings, "323456789132");

    print(res);
    print(res?.stringify());
  }

  Future<void> keyExchange() async {
    try {
      final response = await LibertyEmv.instance.performKeyExchange();
      print(response?.stringify());
    } on PlatformException catch (e) {
      print(e.code.substring(21));
    }
  }

  Future<void> getCardDetails() async {
    print('Ca;;e');
    try {
      final response = await LibertyEmv.instance.getCardDetails();
      print(response?.stringify());
    } on PlatformException catch (e) {
      print(e.code.substring(21));
    }
  }

  Future<void> launchAppStore() async {
    print('Ca;;e');
    try {
      LibertyEmv.instance.launchAppStore();
    } on PlatformException catch (e) {
      print(e.code.substring(21));
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        floatingActionButton: FloatingActionButton(
          onPressed: enquireBalance,
        ),
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('Running on: $_platformVersion\n'),
              ElevatedButton(
                onPressed: getCardDetails,
                child: Text("GetCardDetails"),
              ),
              const SizedBox(
                height: 10,
              ),
              ElevatedButton(
                onPressed: launchAppStore,
                child: Text("Launch App Store"),
              ),
              const SizedBox(
                height: 10,
              ),
              ElevatedButton(
                onPressed: keyExchange,
                child: Text("Keyexchange"),
              ),
              const SizedBox(
                height: 10,
              ),
              ElevatedButton(
                onPressed: enquireBalance,
                child: Text("Balance check"),
              ),
              const SizedBox(
                height: 10,
              ),
              ElevatedButton(
                onPressed: withdrawal,
                child: Text("Withdrawal"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
