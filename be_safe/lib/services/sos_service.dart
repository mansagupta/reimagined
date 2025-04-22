import 'dart:convert';
import 'dart:io';
import 'package:geocoding/geocoding.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:mime/mime.dart';
import 'package:http_parser/http_parser.dart';
import '../main.dart';
import '../utils/constants.dart';
import 'fcm_service.dart';

class SOSService {
  Future<bool> sendSOS(double latitude, double longitude) async {
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      String? token = prefs.getString('token');
      String? username = prefs.getString('username');
      List<String> emergencyContacts = prefs.getStringList('emergencyContacts') ?? [];

      if (token == null || username == null) {
        print("Token not found. Please login again.");
        return false;
      }

      print("Fetching landmark for coordinates: ($latitude, $longitude)...");

      String landmark = await _getLandmark(latitude, longitude);

      print("Sending SOS request to backend...");

      final response = await http.post(
        Uri.parse('$API_BASE_URL/sos/trigger'),
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer $token"
        },
        body: jsonEncode({
          "username": username,
          "latitude": latitude,
          "longitude": longitude,
          "landmark": landmark
        }),
      );

      print("SOS API Response: ${response.statusCode} - ${response.body}");

      if (response.statusCode == 200) {
        print("SOS sent successfully. Now notifying emergency contacts...");

        // Generate Google Maps live location link
        String locationLink = "https://www.google.com/maps?q=$latitude,$longitude";

        // Send notifications to all emergency contacts
        for (String contact in emergencyContacts) {
          await FCMService.sendNotification(
              contact,
              "🚨 SOS Alert!",
              "$username is in danger! 📍Location: $locationLink\nLandmark: $landmark\nPlease check immediately!"
          );
        }
        return true;
      }

      return false;
    } catch (e) {
      print("Error sending SOS: $e");
      return false;
    }
  }

  /// Function to get landmark using reverse geocoding
  Future<String> _getLandmark(double latitude, double longitude) async {
    try {
      List<Placemark> placemarks = await placemarkFromCoordinates(latitude, longitude);
      if (placemarks.isNotEmpty) {
        Placemark place = placemarks.first;
        return "${place.name}, ${place.locality}, ${place.administrativeArea}";
      }
    } catch (e) {
      print("Error fetching landmark: $e");
    }
    return "Unknown Location";
  }


  Future<void> uploadMedia(String filePath) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');

    if (token == null) {
      print("No authorization token found. Please login again.");
      return;
    }

    var uri = Uri.parse("$API_BASE_URL/media/upload");
    var file = File(filePath);

    if (!await file.exists()) {
      print("File does not exist at path: $filePath");
      return;
    }

    var request = http.MultipartRequest('POST', uri);

    request.headers.addAll({
      'Authorization': 'Bearer $token',
      'Content-Type': 'multipart/form-data',
    });

    request.files.add(await http.MultipartFile.fromPath(
      'file',
      filePath,
      contentType: MediaType.parse(lookupMimeType(filePath) ?? 'application/octet-stream'),
    ));

    try {
      var response = await request.send().timeout(Duration(seconds: 30));
      var responseData = await http.Response.fromStream(response);

      if (response.statusCode == 200) {
        print("Media uploaded successfully!");
      } else {
        print("Media upload failed! Status: ${response.statusCode}");
        print("Response: ${responseData.body}");
      }
    } catch (e) {
      print("Error uploading media: $e");
    }
  }
}
