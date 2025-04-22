import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';

import '../main.dart';
import '../utils/constants.dart';

class FCMService {
  static final FirebaseMessaging messaging = FirebaseMessaging.instance;

  static Future<void> registerFCMToken(String mobile) async {
    String? token = await messaging.getToken();
    final prefs = await SharedPreferences.getInstance();
    final mobile = prefs.getString('mobile') ?? '';
    final authToken = prefs.getString('token') ?? '';

    if (token != null && mobile.isNotEmpty) {
      print("FCM Token: $token");
      await http.post(
        Uri.parse('$API_BASE_URL/notifications/register-token'),
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer $authToken",
        },
        body: jsonEncode({"mobile": mobile, "token": token}),
      );
    }
  }

  static Future<void> sendNotification(String recipientUsername, String title, String body) async {
    final prefs = await SharedPreferences.getInstance();
    final authToken = prefs.getString('token') ?? '';

    final response = await http.post(
      Uri.parse('$API_BASE_URL/notifications/send'),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer $authToken",
      },
      body: jsonEncode({
        "username": recipientUsername,
        "title": title,
        "body": body,
      }),
    );

    if (response.statusCode == 200) {
      print("Notification sent successfully: ${response.body}");
    } else {
      print("Failed to send notification: ${response.body}");
    }
  }
}
