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
    LibertyEmv.instance.initialise(Environment.test);
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> enquireBalance() async {
    final res = await LibertyEmv.instance
        .enquireBalance(false, AccountType.savings, "123456789112");

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
                onPressed: keyExchange,
                child: Text("Key Exchange"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
