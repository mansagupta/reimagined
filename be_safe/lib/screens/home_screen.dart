import 'package:flutter/material.dart';
import '../widgets/sos_button.dart';
import '../services/sos_service.dart';
import '../services/location_service.dart';
import 'sos_screen.dart';
import 'location_screen.dart';
import 'contacts_screen.dart';
import 'settings_screen.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  void _sendSOS(BuildContext context) async {
    print(" SOS triggered from HomeScreen, attempting to get location...");

    final location = await LocationService().getCurrentLocation();

    if (location != null) {
      print(" Location retrieved: ${location.latitude}, ${location.longitude}");

      bool success = await SOSService().sendSOS(location.latitude, location.longitude);

      print("SOS request sent, response: $success");

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
      print("Failed to retrieve location.");
    }
  }

  void _navigateToSOS(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) => const SOSScreen()));
  }

  void _navigateToLocation(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) => const LocationScreen()));
  }

  void _navigateToContacts(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) => const ContactsScreen()));
  }

  void _navigateToSettings(BuildContext context) {
    Navigator.push(context, MaterialPageRoute(builder: (context) => const SettingsScreen()));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('SafeAlert')),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
            child: SOSButton(
              onTriggerSOS: () => _sendSOS(context), // Directly send SOS
            ),
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              GestureDetector(
                onTap: () => _navigateToLocation(context),
                child: const Icon(Icons.map, size: 40),
              ),
              GestureDetector(
                onTap: () => _navigateToContacts(context),
                child: const Icon(Icons.contacts, size: 40),
              ),
              GestureDetector(
                onTap: () => _navigateToSettings(context),
                child: const Icon(Icons.settings, size: 40),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
