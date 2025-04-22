import 'package:flutter/material.dart';

class EmergencyCard extends StatelessWidget {
  final String title;
  final IconData icon;
  final VoidCallback onTap;

  const EmergencyCard({super.key, required this.title, required this.icon, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: Icon(icon, color: Colors.red),
        title: Text(title),
        onTap: onTap,
      ),
    );
  }
}
