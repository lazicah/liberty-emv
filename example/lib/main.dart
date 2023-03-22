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
  final _emvApi = EmvApi();

  @override
  void initState() {
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    final res =
        await _emvApi.enquireBalance("8fdsfs", "SAVINGS", "123456789112");
    // final res = await _emvApi.purchase("5", "SAVINGS", "123456789112");
    // final res = await _emvApi.performKeyExchange();
    print(res);
    print(res.stringify());
  }

  Future<void> keyExchange() async {
    try {
      final response = await _emvApi.performKeyExchange();
      print(response.stringify());
    } on PlatformException catch (e) {
      print(e.code.substring(21));
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        floatingActionButton: FloatingActionButton(
          onPressed: initPlatformState,
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
