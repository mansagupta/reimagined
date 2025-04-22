import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:vibration/vibration.dart';
import '../services/sos_service.dart';
import 'dart:io';

class SOSButton extends StatefulWidget {
  final VoidCallback onTriggerSOS;

  const SOSButton({super.key, required this.onTriggerSOS});

  @override
  _SOSButtonState createState() => _SOSButtonState();
}

class _SOSButtonState extends State<SOSButton> {
  CameraController? _cameraController;
  bool _isRecording = false;
  bool _isCameraInitialized = false; 
  String? _videoPath;
  final SOSService _sosService = SOSService();

  @override
  void initState() {
    super.initState();
    _requestPermissions().then((granted) {
      if (granted) _initializeCamera();
    });
  }

  Future<bool> _requestPermissions() async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.camera,
      Permission.microphone,
      Permission.storage
    ].request();

    return statuses.values.every((status) => status.isGranted);
  }

  Future<void> _initializeCamera() async {
    try {
      final cameras = await availableCameras();
      if (cameras.isEmpty) {
        print("No cameras found!");
        return;
      }

      _cameraController = CameraController(
        cameras[0],
        ResolutionPreset.high,
        enableAudio: true,
      );

      await _cameraController!.initialize();
      if (!mounted) return;

      setState(() {
        _isCameraInitialized = true; // Mark camera as initialized
      });

      print("📷 Camera initialized successfully!");
    } catch (e) {
      print("Error initializing camera: $e");
    }
  }

  Future<String> _getSavePath() async {
    Directory? directory = Directory('/storage/emulated/0/Movies/');
    if (!await directory.exists()) {
      directory = await getApplicationDocumentsDirectory();
    }
    return '${directory.path}/sos_video_${DateTime.now().millisecondsSinceEpoch}.mp4';
  }

  Future<void> _startRecording() async {
    if (_cameraController == null || !_cameraController!.value.isInitialized) {
      print("Camera not initialized, trying to initialize...");
      await _initializeCamera(); // Ensure camera is ready
      await Future.delayed(const Duration(seconds: 1)); // Wait for camera
    }

    if (!_isCameraInitialized) {
      print("Camera still not initialized! Aborting recording.");
      return;
    }

    if (_isRecording) {
      print("Already recording...");
      return;
    }

    try {
      final filePath = await _getSavePath();

      print("Starting video recording...");
      await _cameraController!.startVideoRecording();

      setState(() {
        _isRecording = true;
        _videoPath = filePath;
      });

      print("Recording started!");
    } catch (e) {
      print("Error starting recording: $e");
    }
  }

  Future<void> _stopRecording() async {
    if (!_isRecording || _cameraController == null) return;

    try {
      final file = await _cameraController!.stopVideoRecording();
      final newPath = await _getSavePath();
      await file.saveTo(newPath);
      print("📁 Video saved at: $newPath");
      _videoPath = newPath;

      setState(() {
        _isRecording = false;
      });

      if (mounted) {
        await _initializeCamera();
      }

      if (_videoPath != null) {
        await _sosService.uploadMedia(_videoPath!);
      }
    } catch (e) {
      print("Error stopping recording: $e");
    }
  }

  void _triggerSOS() async {
    if (await Vibration.hasVibrator() ?? false) {
      Vibration.vibrate(duration: 500);
    }

    print("SOS Activated!");

    await _startRecording();
    widget.onTriggerSOS();
  }

  @override
  void dispose() {
    _cameraController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onDoubleTap: _triggerSOS,
      onTap: () {
        if (_isRecording) {
          _stopRecording();
        }
      },
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        width: 150,
        height: 150,
        decoration: BoxDecoration(
          color: _isRecording ? Colors.redAccent : Colors.red,
          shape: BoxShape.circle,
          boxShadow: [
            BoxShadow(
              color: Colors.red.withOpacity(0.5),
              blurRadius: 20,
              spreadRadius: 5,
            ),
          ],
        ),
        child: Center(
          child: Text(
            _isRecording ? 'Recording...' : 'SOS',
            style: const TextStyle(
                fontSize: 24, color: Colors.white, fontWeight: FontWeight.bold),
          ),
        ),
      ),
    );
  }
}
