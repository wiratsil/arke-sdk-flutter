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
  bool _isConnected = false;
  int _beepDuration = 1000;
  int _selectedAlign = 1; // 0=Left, 1=Center, 2=Right
  String _statusMessage = 'Initializing...';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      platformVersion = await _arkeSdkFlutterPlugin.getPlatformVersion() ?? 'Unknown platform version';
      _isConnected = true;
      _statusMessage = 'SDK Connected';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
      _isConnected = false;
      _statusMessage = 'SDK Connection Failed';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
    
    // Auto fetch info if connected
    if (_isConnected) {
      _getTerminalInfo();
    }
  }

  Future<void> _beep() async {
    setState(() => _statusMessage = 'Beeping for ${_beepDuration}ms...');
    try {
      await _arkeSdkFlutterPlugin.beep(milliseconds: _beepDuration);
      setState(() => _statusMessage = 'Beep Success');
    } catch (e) {
      setState(() => _statusMessage = 'Beep Error: $e');
    }
  }

  Future<void> _getTerminalInfo() async {
    setState(() => _statusMessage = 'Fetching Terminal Info...');
    try {
      final info = await _arkeSdkFlutterPlugin.getTerminalInfo();
      if (info != null) {
        setState(() {
          _terminalInfo = info;
          _statusMessage = 'Info Fetched';
        });
      }
    } catch (e) {
      setState(() => _statusMessage = 'Fetch Error: $e');
    }
  }

  Future<void> _printTest() async {
    setState(() => _statusMessage = 'Printing...');
    try {
      await _arkeSdkFlutterPlugin.printText("--- ARKE SDK TEST ---", align: _selectedAlign);
      await _arkeSdkFlutterPlugin.printText("Model: ${_terminalInfo['model'] ?? 'N/A'}", align: 0);
      await _arkeSdkFlutterPlugin.printText("Serial: ${_terminalInfo['serialNo'] ?? 'N/A'}", align: 0);
      await _arkeSdkFlutterPlugin.printText("OS: $_platformVersion", align: 0);
      await _arkeSdkFlutterPlugin.printText("\nFlutter Plugin Test Succeed!\n", align: 1);
      await _arkeSdkFlutterPlugin.printText("----------------------", align: 1);
      setState(() => _statusMessage = 'Print Success');
    } catch (e) {
      setState(() => _statusMessage = 'Print Error: $e');
    }
  }

  Future<void> _startScanner() async {
    setState(() => _statusMessage = 'Starting Scanner...');
    try {
      final code = await _arkeSdkFlutterPlugin.startScanner();
      setState(() => _statusMessage = 'Scan Result: $code');
    } catch (e) {
      setState(() => _statusMessage = 'Scan Error: $e');
    }
  }

  Future<void> _startNfcScan() async {
    setState(() => _statusMessage = 'Please tap card...');
    try {
      final uid = await _arkeSdkFlutterPlugin.startNfcScan();
      setState(() => _statusMessage = 'Card UID: $uid');
    } catch (e) {
      setState(() => _statusMessage = 'NFC Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        useMaterial3: true, 
        colorSchemeSeed: Colors.indigo,
        brightness: Brightness.light,
      ),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Arke SDK Tester'),
          centerTitle: true,
          actions: [
            IconButton(
              onPressed: initPlatformState,
              icon: const Icon(Icons.refresh),
            )
          ],
        ),
        body: SingleChildScrollView(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              _buildStatusCard(),
              const SizedBox(height: 16),
              _buildInfoCard(),
              const SizedBox(height: 16),
              _buildBeeperControls(),
              const SizedBox(height: 16),
              _buildPrinterControls(),
              const SizedBox(height: 16),
              _buildScannerControls(),
              const SizedBox(height: 16),
              _buildNfcControls(),
              const SizedBox(height: 24),
              Text(
                'Status: $_statusMessage',
                style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.indigo),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildStatusCard() {
    return Card(
      color: _isConnected ? Colors.green.shade50 : Colors.red.shade50,
      child: ListTile(
        leading: Icon(
          _isConnected ? Icons.check_circle : Icons.error,
          color: _isConnected ? Colors.green : Colors.red,
        ),
        title: Text(_isConnected ? 'Device Connected' : 'Device Disconnected'),
        subtitle: Text('Android OS: $_platformVersion'),
      ),
    );
  }

  Widget _buildInfoCard() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('Terminal Information', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                IconButton(
                  onPressed: _getTerminalInfo,
                  icon: const Icon(Icons.download, size: 20),
                ),
              ],
            ),
            const Divider(),
            _infoRow('Model', _terminalInfo['model'] ?? 'N/A'),
            _infoRow('Serial No', _terminalInfo['serialNo'] ?? 'N/A'),
            _infoRow('ROM', _terminalInfo['romVersion'] ?? 'N/A'),
            _infoRow('Firmware', _terminalInfo['firmwareVersion'] ?? 'N/A'),
          ],
        ),
      ),
    );
  }

  Widget _infoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(color: Colors.grey)),
          Text(value, style: const TextStyle(fontWeight: FontWeight.w500)),
        ],
      ),
    );
  }

  Widget _buildBeeperControls() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Beeper Test', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const Divider(),
            Row(
              children: [
                const Text('Duration: '),
                Expanded(
                  child: Slider(
                    value: _beepDuration.toDouble(),
                    min: 100,
                    max: 3000,
                    divisions: 29,
                    label: '${_beepDuration}ms',
                    onChanged: (val) => setState(() => _beepDuration = val.round()),
                  ),
                ),
                Text('${_beepDuration}ms'),
              ],
            ),
            ElevatedButton.icon(
              onPressed: _isConnected ? _beep : null,
              icon: const Icon(Icons.volume_up),
              label: const Text('Play Sound'),
              style: ElevatedButton.styleFrom(minimumSize: const Size.fromHeight(40)),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildPrinterControls() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Printer Test', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const Divider(),
            const Text('Alignment:', style: TextStyle(fontSize: 14)),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                _alignOption(0, 'Left'),
                _alignOption(1, 'Center'),
                _alignOption(2, 'Right'),
              ],
            ),
            const SizedBox(height: 8),
            ElevatedButton.icon(
              onPressed: _isConnected ? _printTest : null,
              icon: const Icon(Icons.print),
              label: const Text('Print Test Receipt'),
              style: ElevatedButton.styleFrom(
                minimumSize: const Size.fromHeight(40),
                backgroundColor: Colors.indigo.shade100,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _alignOption(int value, String label) {
    return Row(
      children: [
        Radio<int>(
          value: value,
          groupValue: _selectedAlign,
          onChanged: (val) => setState(() => _selectedAlign = val!),
          visualDensity: VisualDensity.compact,
        ),
        Text(label),
      ],
    );
  }

  Widget _buildScannerControls() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Scanner Test', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const Divider(),
            ElevatedButton.icon(
              onPressed: _isConnected ? _startScanner : null,
              icon: const Icon(Icons.qr_code_scanner),
              label: const Text('Scan Barcode/QR'),
              style: ElevatedButton.styleFrom(minimumSize: const Size.fromHeight(40)),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildNfcControls() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('NFC / Card Tap Test', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
            const Divider(),
            ElevatedButton.icon(
              onPressed: _isConnected ? _startNfcScan : null,
              icon: const Icon(Icons.tap_and_play),
              label: const Text('Read NFC Card'),
              style: ElevatedButton.styleFrom(minimumSize: const Size.fromHeight(40)),
            ),
          ],
        ),
      ),
    );
  }
}
