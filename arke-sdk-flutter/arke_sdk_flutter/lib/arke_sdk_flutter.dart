
import 'arke_sdk_flutter_platform_interface.dart';

class ArkeSdkFlutter {
  Future<String?> getPlatformVersion() {
    return ArkeSdkFlutterPlatform.instance.getPlatformVersion();
  }

  Future<void> beep({int milliseconds = 500}) {
    return ArkeSdkFlutterPlatform.instance.beep(milliseconds: milliseconds);
  }

  Future<void> printText(String text, {int align = 0}) {
    return ArkeSdkFlutterPlatform.instance.printText(text, align: align);
  }

  Future<Map<String, String>?> getTerminalInfo() {
    return ArkeSdkFlutterPlatform.instance.getTerminalInfo();
  }
}
