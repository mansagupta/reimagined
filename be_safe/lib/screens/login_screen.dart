import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../services/auth_service.dart';
import '../services/fcm_service.dart';
import 'forget_password_screen.dart';


class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  void _login() async {
    String mobile = mobileController.text.trim();
    String password = passwordController.text;

    bool success = await AuthService().login(mobile, password);

    if (success) {
      FCMService.registerFCMToken(mobile);
      SharedPreferences prefs = await SharedPreferences.getInstance();
      String? storedMobile = prefs.getString('mobile');

      print("Login Successful! Username: $storedMobile");

      Navigator.pushReplacementNamed(context, '/home');
    } else {
      print("Login failed");
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Login failed. Please try again.'))
      );
    }
  }

  void _navigateToRegistration() {
    Navigator.pushNamed(context, '/register');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Login')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: mobileController,
              decoration: const InputDecoration(labelText: 'Mobile'),
            ),
            TextField(
              controller: passwordController,
              obscureText: true,
              decoration: const InputDecoration(labelText: 'Password'),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _login,
              child: const Text('Login'),
            ),
            const SizedBox(height: 10),
            TextButton(
              onPressed: _navigateToRegistration,
              child: const Text('Don\'t have an account? Register here'),
            ),
            TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => ForgotPasswordScreen()),
                );
              },
              child: Text("Forgot Password?"),
            )

          ],
        ),
      ),
    );
  }
}
