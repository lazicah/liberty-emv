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
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Plugin example app',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        debugShowCheckedModeBanner: false,
        localizationsDelegates: const [
          DefaultMaterialLocalizations.delegate,
        ],
        home: const AppBodyWidget());
  }
}

class AppBodyWidget extends StatefulWidget {
  const AppBodyWidget({super.key});

  @override
  State<AppBodyWidget> createState() => _AppBodyWidgetState();
}

class _AppBodyWidgetState extends State<AppBodyWidget> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    LibertyEmv.instance.initialise(Environment.live);
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> enquireBalance() async {
    try {
      final res = await LibertyEmv.instance.enquireBalance(
          false,
          AccountType.savings,
          DateTime.now().millisecondsSinceEpoch.toString().substring(0, 12));
      print(res);
      print(res?.stringify());
    } on PlatformException catch (e) {
      // Handle platform-specific error
      print('PlatformException caught:');
      print('Code: ${e.code}');
      print('Message: ${e.message}');
      print('Details: ${e.details}');
    } catch (e) {
      // Handle any other errors
      print('Other exception: $e');
    }
  }

  Future<void> withdrawal() async {
    try {
      final res = await LibertyEmv.instance
          .purchase(10000000000, AccountType.savings, "323456789132");

      print(res);
      print(res?.stringify());
    } on PlatformException catch (e) {
      // Handle platform-specific error
      print('PlatformException caught:');
      print('Code: ${e.code}');
      print('Message: ${e.message}');
      print('Details: ${e.details}');
    } catch (e) {
      // Handle any other errors
      print('Other exception: $e');
    }
  }

  Future<void> keyExchange() async {
    try {
      final response = await LibertyEmv.instance.performKeyExchange();
      print(response.stringify());
      ScaffoldMessenger.of(context)
        ..showSnackBar(
            SnackBar(content: Text(response.responseMessage ?? "NA")));
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  Future<void> getCardDetails() async {
    print('Get Card details');
    try {
      final response = await LibertyEmv.instance.getCardDetails();
      print(response?.stringify());
      print('Card response caught:');
    } on PlatformException catch (e) {
      print(e.message);
      print('PlatformException caught:');
    }
  }

  Future<void> getSerialNo() async {
    try {
      final response = await LibertyEmv.instance.getSerialNo();
      print(response);
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  Future<void> launchAppStore() async {
    print('Ca;;e');
    try {
      LibertyEmv.instance.launchAppStore();
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
              child: const Text("GetCardDetails"),
            ),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
              onPressed: launchAppStore,
              child: const Text("Launch App Store"),
            ),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
              onPressed: keyExchange,
              child: const Text("Keyexchange"),
            ),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
              onPressed: enquireBalance,
              child: const Text("Balance check"),
            ),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
              onPressed: withdrawal,
              child: const Text("Withdrawal"),
            ),
            const SizedBox(
              height: 10,
            ),
            ElevatedButton(
              onPressed: getSerialNo,
              child: const Text("Serial No"),
            ),
          ],
        ),
      ),
    );
  }
}
