import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'arke_sdk_flutter_platform_interface.dart';

/// An implementation of [ArkeSdkFlutterPlatform] that uses method channels.
class MethodChannelArkeSdkFlutter extends ArkeSdkFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('arke_sdk_flutter');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> beep({int milliseconds = 500}) async {
    await methodChannel.invokeMethod<void>('beep', {'milliseconds': milliseconds});
  }

  @override
  Future<void> printText(String text, {int align = 0}) async {
    await methodChannel.invokeMethod<void>('printText', {'text': text, 'align': align});
  }

  @override
  Future<Map<String, String>?> getTerminalInfo() async {
    final info = await methodChannel.invokeMapMethod<String, String>('getTerminalInfo');
    return info;
  }
}
