import 'package:flutter/material.dart';
import '../services/sos_service.dart';
import '../services/location_service.dart';
import '../widgets/sos_button.dart';

class SOSScreen extends StatefulWidget {
  const SOSScreen({super.key});

  @override
  _SOSScreenState createState() => _SOSScreenState();
}

class _SOSScreenState extends State<SOSScreen> {
  bool _isSending = false;

  void _sendSOS() async {
    print("🔴 SOS button pressed, attempting to get location...");
    setState(() {
      _isSending = true;
    });

    final location = await LocationService().getCurrentLocation();

    if (location != null) {
      print("📍 Location retrieved: ${location.latitude}, ${location.longitude}");

      bool success = await SOSService().sendSOS(location.latitude, location.longitude);

      print("📡 SOS request sent, response: $success");

      if (success) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('SOS Alert Sent!')),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to send SOS')),
        );
      }
    } else {
      print("❌ Failed to retrieve location.");
    }

    setState(() {
      _isSending = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Emergency SOS')),
      body: Center(
        child: _isSending
            ? const CircularProgressIndicator()
            : SOSButton(
          onTriggerSOS: _sendSOS,
        ),
      ),
    );
  }
}
