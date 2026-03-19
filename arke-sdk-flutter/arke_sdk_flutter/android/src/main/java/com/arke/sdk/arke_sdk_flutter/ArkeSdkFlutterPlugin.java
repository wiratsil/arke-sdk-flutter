package com.arke.sdk.arke_sdk_flutter;

import android.content.Context;
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** ArkeSdkFlutterPlugin */
public class ArkeSdkFlutterPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    com.arke.sdk.ArkeSdkDemoApplication.init(context);
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "arke_sdk_flutter");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("beep")) {
      int milliseconds = call.argument("milliseconds") != null ? (int) call.argument("milliseconds") : 500;
      try {
        com.arke.sdk.api.Beeper.getInstance().startBeep(milliseconds);
        result.success(null);
      } catch (Exception e) {
        result.error("SDK_ERROR", e.getMessage(), null);
      }
    } else if (call.method.equals("printText")) {
      String text = call.argument("text");
      int align = call.argument("align") != null ? (int) call.argument("align") : 0;
      try {
        com.arke.sdk.util.printer.Printer printer = com.arke.sdk.util.printer.Printer.getInstance();
        printer.getStatus();
        printer.addText(align, text);
        printer.feedLine(5);
        printer.start(new com.usdk.apiservice.aidl.printer.OnPrintListener.Stub() {
          @Override
          public void onFinish() throws android.os.RemoteException {
            result.success(null);
          }

          @Override
          public void onError(int errorCode) throws android.os.RemoteException {
            result.error("PRINTER_ERROR", com.arke.sdk.util.printer.Printer.getErrorMessage(errorCode), null);
          }
        });
      } catch (Exception e) {
        result.error("SDK_ERROR", e.getMessage(), null);
      }
    } else if (call.method.equals("getTerminalInfo")) {
      try {
        com.arke.sdk.api.DeviceManager deviceManager = com.arke.sdk.api.DeviceManager.getInstance();
        java.util.Map<String, String> info = new java.util.HashMap<>();
        info.put("model", deviceManager.getModel());
        info.put("serialNo", deviceManager.getSerialNo());
        info.put("osVersion", deviceManager.getAndroidOSVersion());
        info.put("romVersion", deviceManager.getRomVersion());
        info.put("firmwareVersion", deviceManager.getFirmwareVersion());
        info.put("hardwareVersion", deviceManager.getHardwareVersion());
        result.success(info);
      } catch (Exception e) {
        result.error("SDK_ERROR", e.getMessage(), null);
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
