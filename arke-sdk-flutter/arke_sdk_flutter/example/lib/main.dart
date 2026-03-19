import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:arke_sdk_flutter/arke_sdk_flutter.dart';

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
  final _arkeSdkFlutterPlugin = ArkeSdkFlutter();
  Map<String, String> _terminalInfo = {};

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      platformVersion = await _arkeSdkFlutterPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> _beep() async {
    try {
      await _arkeSdkFlutterPlugin.beep(milliseconds: 1000);
    } catch (e) {
      debugPrint('Beep error: $e');
    }
  }

  Future<void> _getTerminalInfo() async {
    try {
      final info = await _arkeSdkFlutterPlugin.getTerminalInfo();
      if (info != null) {
        setState(() {
          _terminalInfo = info;
        });
      }
    } catch (e) {
      debugPrint('GetTerminalInfo error: $e');
    }
  }

  Future<void> _printTest() async {
    try {
      await _arkeSdkFlutterPlugin.printText("--- ARKE SDK TEST ---", align: 1);
      await _arkeSdkFlutterPlugin.printText("Model: ${_terminalInfo['model'] ?? 'N/A'}", align: 0);
      await _arkeSdkFlutterPlugin.printText("Serial: ${_terminalInfo['serialNo'] ?? 'N/A'}", align: 0);
      await _arkeSdkFlutterPlugin.printText("\nFlutter Plugin Test Succeed!\n", align: 1);
      await _arkeSdkFlutterPlugin.printText("----------------------", align: 1);
    } catch (e) {
      debugPrint('Print error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(useMaterial3: true, colorSchemeSeed: Colors.blue),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Arke SDK Plugin Test'),
          centerTitle: true,
        ),
        body: SingleChildScrollView(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    children: [
                      Text('OS: $_platformVersion', style: const TextStyle(fontWeight: FontWeight.bold)),
                      const SizedBox(height: 8),
                      if (_terminalInfo.isNotEmpty) ...[
                        Text('Model: ${_terminalInfo['model']}'),
                        Text('SN: ${_terminalInfo['serialNo']}'),
                      ],
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton.icon(
                onPressed: _beep,
                icon: const Icon(Icons.volume_up),
                label: const Text('Test Beep (1s)'),
              ),
              const SizedBox(height: 12),
              ElevatedButton.icon(
                onPressed: _getTerminalInfo,
                icon: const Icon(Icons.info_outline),
                label: const Text('Fetch Terminal Info'),
              ),
              const SizedBox(height: 12),
              ElevatedButton.icon(
                onPressed: _printTest,
                icon: const Icon(Icons.print),
                label: const Text('Print Test Receipt'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blue.shade100,
                ),
              ),
              const SizedBox(height: 32),
              const Text(
                'Note: Features require physical Arke POS device with USDK service installed.',
                style: TextStyle(fontSize: 12, color: Colors.grey, fontStyle: FontStyle.italic),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
