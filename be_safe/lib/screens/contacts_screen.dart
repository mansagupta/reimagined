import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:contacts_service/contacts_service.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../services/emergency_contact_service.dart';

class ContactsScreen extends StatefulWidget {
  const ContactsScreen({super.key});

  @override
  _ContactsScreenState createState() => _ContactsScreenState();
}

class _ContactsScreenState extends State<ContactsScreen> {
  final EmergencyContactService _contactService = EmergencyContactService();
  List<Map<String, String>> _contacts = [];
  String? _mobile;
  late FirebaseMessaging messaging;
  FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();


  @override
  void initState() {
    super.initState();
    _loadUserAndContacts();
    messaging = FirebaseMessaging.instance;
    _requestPermissions();
    _configureFCM();
  }

  Future<void> _loadUserAndContacts() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? mobile = prefs.getString('mobile');

    if (mobile != null) {
      setState(() {
        _mobile = mobile;
      });

      _loadContacts(mobile);
    } else {
      print("Mobile number not found in SharedPreferences");
    }
  }


  Future<void> _loadContacts(String mobile) async {
    try {
      List<Map<String, String>> contacts = await _contactService.getEmergencyContacts();
      setState(() {
        _contacts = contacts;
      });
    } catch (e) {
      print("Error loading contacts: $e");
    }
  }

  Future<void> _addContact() async {
    Future.delayed(Duration(milliseconds: 500), () async {
      await _pickContact();
    });
  }

  Future<void> _pickContact() async {
    if (_mobile == null) return;

    if (await Permission.contacts.request().isGranted) {
      try {
        Contact? contact = await ContactsService.openDeviceContactPicker();

        if (contact != null && contact.phones != null && contact.phones!.isNotEmpty) {
          String name = contact.displayName ?? "Unknown";
          String phone = contact.phones!.first.value ?? "";

          await _contactService.addEmergencyContact(name, phone);
          _loadContacts(_mobile!);
        } else {
          print("Selected contact has no phone number.");
        }
      } catch (e) {
        print("Error selecting contact: $e");
      }
    } else {
      print("Permission denied.");
    }
  }


  Future<void> _removeContact(String phone) async {
    if (_mobile == null) return;

    await _contactService.deleteEmergencyContact(phone);
    _loadContacts(_mobile!);
  }

  void _requestPermissions() async {
    NotificationSettings settings = await messaging.requestPermission(
      alert: true,
      badge: true,
      sound: true,
    );
    print('User granted permission: ${settings.authorizationStatus}');
  }

  void _configureFCM() {
    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      print("New notification: ${message.notification?.title}");
      _showNotification(message);
    });

    FirebaseMessaging.onMessageOpenedApp.listen((RemoteMessage message) {
      print("Notification clicked: ${message.data}");
    });
  }

  Future<void> _showNotification(RemoteMessage message) async {
    const AndroidNotificationDetails androidPlatformChannelSpecifics =
    AndroidNotificationDetails(
      'sos_channel',
      'SOS Notifications',
      importance: Importance.max,
      priority: Priority.high,
      showWhen: false,
    );
    const NotificationDetails platformChannelSpecifics =
    NotificationDetails(android: androidPlatformChannelSpecifics);

    await flutterLocalNotificationsPlugin.show(
      0,
      message.notification?.title,
      message.notification?.body,
      platformChannelSpecifics,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Emergency Contacts')),
      body: _mobile == null
          ? const Center(child: CircularProgressIndicator()) // Show loader while fetching username
          : ListView.builder(
        itemCount: _contacts.length,
        itemBuilder: (context, index) {
          return ListTile(
            title: Text(_contacts[index]['name']!),
            subtitle: Text(_contacts[index]['phone']!),
            trailing: IconButton(
              icon: const Icon(Icons.delete, color: Colors.red),
              onPressed: () => _removeContact(_contacts[index]['phone']!),
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _addContact,
        child: const Icon(Icons.add),
      ),
    );
  }
}
