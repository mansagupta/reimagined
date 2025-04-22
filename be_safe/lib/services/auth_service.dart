import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../main.dart';
import '../utils/constants.dart';

class AuthService {

  Future<String?> register(String username, String email, String password, String mobile) async {
    final url = Uri.parse('$API_BASE_URL/auth/register');
    final response = await http.post(
      url,
      headers: {"Content-Type": "application/json", "Accept": "application/json"},
      body: jsonEncode({
        "username": username,
        "email": email,
        "password": password,
        "mobile": mobile
      }),
    );

    final data = jsonDecode(response.body);
    final message = data['message'] ?? "Registration Failed. Try again!";

    if (response.statusCode == 200) {
      final token = data['token'] ?? "";
      final storedUsername = data['username'] ?? "";

      SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('token', token);
      prefs.setString('username', storedUsername);
      prefs.setBool('isLoggedIn', true);
      return null;
    } else{
      return message;
    }
  }

  // User Login
  Future<bool> login(String mobile, String password) async {
    final url = Uri.parse('$API_BASE_URL/auth/login');

    try {
      final response = await http.post(
        url,
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: jsonEncode({"mobile": mobile, "password": password}),
      );

      print("Response status: ${response.statusCode}");
      print("Response body: ${response.body}");

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);

        if (!data.containsKey('mobile') || !data.containsKey('token')) {
          throw Exception("Invalid response format. Expected 'mobile' and 'token'.");
        }

        final token = data['token'];
        final mobile = data['mobile'];

        SharedPreferences prefs = await SharedPreferences.getInstance();
        prefs.setString('token', token);
        prefs.setBool('isLoggedIn', true);
        prefs.setString('mobile', mobile);

        await fetchAndSaveUsername(mobile);

        return true;
      } else {
        final errorResponse = jsonDecode(response.body);
        print("Login failed: ${errorResponse['message']}");
        return false;
      }
    } catch (e) {
      print("Login error: $e");
      return false;
    }
  }

  Future<Map<String, String>> _getAuthData() async {
    final prefs = await SharedPreferences.getInstance();
    final mobile = prefs.getString('mobile') ?? '';
    final token = prefs.getString('token') ?? '';

    if (mobile.isEmpty || token.isEmpty) {
      throw Exception('User not authenticated');
    }

    return {'mobile': mobile, 'token': token};
  }

  Future<void> fetchAndSaveUsername(String mobile) async {
    try {
      final authData = await _getAuthData();
      final response = await http.post(
        Uri.parse('$API_BASE_URL/auth/username'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ${authData['token']}',
        },
        body: jsonEncode([
          {'mobile': mobile}
        ]),
      );

      if (response.statusCode == 200) {
        String username = response.body;

        SharedPreferences prefs = await SharedPreferences.getInstance();
        await prefs.setString('username', username);
        print('Username saved: $username');
      } else {
        print('Failed to fetch username: ${response.body}');
      }
    } catch (e) {
      print('Error fetching username: $e');
    }
  }


  // Get Logged-in Username
  Future<String?> getLoggedInMobile() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    if (prefs.getBool('isLoggedIn') ?? false) {
      return prefs.getString('mobile');
    }
    return null;
  }

  // User Logout
  Future<void> logout() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.remove('token');
    prefs.remove('mobile');
    prefs.setBool('isLoggedIn', false);
  }

  // Check if User is Logged In
  Future<bool> isLoggedIn() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getBool('isLoggedIn') ?? false;
  }
}
