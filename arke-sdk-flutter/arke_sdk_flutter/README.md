# arke_sdk_flutter

A Flutter plugin for the Arke SDK (Android), enabling interaction with Arke POS device hardware.

## Installation via Git

Add `arke_sdk_flutter` as a dependency in your `pubspec.yaml`:

```yaml
dependencies:
  arke_sdk_flutter:
    git:
      url: https://github.com/wiratsil/arke-sdk-flutter.git
      path: arke-sdk-flutter/arke_sdk_flutter
```

## Features

- **Beeper**: Control the device's beeper.
- **Printer**: Thermal printing (Text, Alignment).
- **Terminal Info**: Retrieve Serial Number, Model, and OS versions.

## Usage

### Beeper

```dart
final _arkeSdkPlugin = ArkeSdkFlutter();
await _arkeSdkPlugin.beep(milliseconds: 1000);
```

### Printer

```dart
await _arkeSdkPlugin.printText("Hello Arke!", align: 1); // 0=Left, 1=Center, 2=Right
```

### Terminal Info

```dart
final info = await _arkeSdkPlugin.getTerminalInfo();
print("Model: ${info?['model']}");
```

---

## SDK Version Note
This plugin is developed based on USDK API version `2.1.3`.
It requires a physical Arke POS device with the `USDK Service` installed.

