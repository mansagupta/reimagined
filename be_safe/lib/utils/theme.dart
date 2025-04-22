import 'package:flutter/material.dart';

final ThemeData appTheme = ThemeData(
  primaryColor: Colors.red,
  colorScheme: ColorScheme.fromSwatch().copyWith(
    secondary: Colors.redAccent,
  ),
  buttonTheme: const ButtonThemeData(buttonColor: Colors.red),
);
