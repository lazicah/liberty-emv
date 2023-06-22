# Liberty Emv

# Flutter Section

## What it Does ?

It’s a Flutter plugin created for the purpose of processing card transactions. This plugin creates an bridge between the flutter project and  the  native android “LibertyEmv” SDK, created in house. 

## Setup

```yaml
dependencies:
	liberty_emv:
    git:
      url: https://github.com/emem785/liberty-emv.git
      ref: main
```

Currently the plugin is primarily hosted on “Github”, in order to add it as a dependency to your project you have to specify some parameter’s in the “pubspec” file.

Reference this stack overflow post for more information: [https://stackoverflow.com/questions/54022704/how-to-add-a-package-from-github-in-flutter](https://stackoverflow.com/questions/54022704/how-to-add-a-package-from-github-in-flutter)

## Usage

Before a card transaction can take place, some steps must be taken:

1. First, perform the "Params Download" step. This downloads the parameters necessary for card transactions and only needs to be done once.
2. Second, perform the "Key Exchange" step. In this step, encryption keys are exchanged with the server that will eventually handle the processing of the transactions.

```dart
Future<KeyExchangeResponse> performKeyExchange()
```

This method initiates the “keyExchange” process. In this plugin the param download is done automatically, so no method is needed for that step.

```dart
Future<TransactionDataResponse> enquireBalance(String arg_tID, String arg_accountType, String arg_rrn) 
```

This method is used to check the **balance of the customers account**

```dart
Future<TransactionDataResponse> purchase(String arg_amount, String arg_accountType, String arg_rrn) async {
```

This method is used to actually initiate a card transaction. some of the required parameters are:

1. amount 
2. account Type (Savings,Current, etc)
3. RRN (This is a unique identifier used to track card transactions)

## Pigeon

The pigeon package was used in the creation of this package, it aids in creating strongly typed classes that can be passed through method channels. You can find more information here:

[https://pub.dev/packages/pigeon](https://pub.dev/packages/pigeon)

# Android Section

## Introduction

Before making adjustments to the android layer of this plugin, you have to be conversant with how to build flutter plugins you can check here for some resources:

[https://docs.flutter.dev/packages-and-plugins/developing-packages#:~:text=To create a plugin](https://docs.flutter.dev/packages-and-plugins/developing-packages#:~:text=To%20create%20a%20plugin%20package,linux%20%2C%20macos%20%2C%20and%20windows%20) [package,linux %2C macos %2C and windows .](https://docs.flutter.dev/packages-and-plugins/developing-packages)

## Setup

```groovy
configurations.maybeCreate("default")
artifacts.add("default", file('horizonpay-release-1.0.1.aar'))
```

The  **HorizonPay** SDK is added to the project as an “aar” dependency, the aar is located in a separate module 

```groovy
configurations.maybeCreate("default")
artifacts.add("default", file('pos-SDK-0.4.0.aar'))
```

The LibertyEmv SDK is added to the project as an “aar” dependency, the aar is located in a separate module and it depends on the HorizonPay SDK

```groovy
implementation project(path: ':horizon_isw')
implementation project(path: ':pos_emv')
```

The module’s containing the aar file’s is added to the project as a dependency

## Usage

```groovy
class EmvService(private val context: Context)
```

This class holds the implementation logic for the plugin, it extends the `PluginRegistry.ActivityResultListener` class

```groovy
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean 
```

All processes that involve debiting the customer’s card, get their responses from the “onActivityResult” callback method