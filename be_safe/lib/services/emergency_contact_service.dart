import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../main.dart';
import '../utils/constants.dart';

class EmergencyContactService {
  final String baseUrl = '$API_BASE_URL/emergency';

  Future<Map<String, String>> _getAuthData() async {
    final prefs = await SharedPreferences.getInstance();
    final mobile = prefs.getString('mobile') ?? '';
    final token = prefs.getString('token') ?? '';

    if (mobile.isEmpty || token.isEmpty) {
      throw Exception('User not authenticated');
    }

    return {'mobile': mobile, 'token': token};
  }

  Future<List<Map<String, String>>> getEmergencyContacts() async {
    final authData = await _getAuthData();
    final response = await http.get(
      Uri.parse('$baseUrl/get?mobile=${authData['mobile']}'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${authData['token']}',
      },
    );

    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((contact) => {
        'name': contact['name'].toString(),
        'phone': contact['phone'].toString(),
      }).toList();
    } else {
      throw Exception('Failed to load contacts');
    }
  }

  Future<void> addEmergencyContact(String name, String phone) async {
    final authData = await _getAuthData();
    final response = await http.post(
      Uri.parse('$baseUrl/save?mobile=${authData['mobile']}'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${authData['token']}',
      },
      body: jsonEncode([
        {'name': name, 'phone': phone}
      ]),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to add contact');
    }
  }

  Future<void> deleteEmergencyContact(String phone) async {
    final authData = await _getAuthData();
    final response = await http.delete(
      Uri.parse('$baseUrl/${authData['mobile']}/$phone'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${authData['token']}',
      },
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to delete contact');
    }
  }
}
