import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'arke_sdk_flutter_method_channel.dart';

abstract class ArkeSdkFlutterPlatform extends PlatformInterface {
  /// Constructs a ArkeSdkFlutterPlatform.
  ArkeSdkFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static ArkeSdkFlutterPlatform _instance = MethodChannelArkeSdkFlutter();

  /// The default instance of [ArkeSdkFlutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelArkeSdkFlutter].
  static ArkeSdkFlutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ArkeSdkFlutterPlatform] when
  /// they register themselves.
  static set instance(ArkeSdkFlutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> beep({int milliseconds = 500}) {
    throw UnimplementedError('beep() has not been implemented.');
  }

  Future<void> printText(String text, {int align = 0}) {
    throw UnimplementedError('printText() has not been implemented.');
  }

  Future<Map<String, String>?> getTerminalInfo() {
    throw UnimplementedError('getTerminalInfo() has not been implemented.');
  }
}
