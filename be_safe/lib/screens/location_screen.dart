import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:geocoding/geocoding.dart';
import '../services/location_service.dart';

class LocationScreen extends StatefulWidget {
  const LocationScreen({super.key});

  @override
  _LocationScreenState createState() => _LocationScreenState();
}

class _LocationScreenState extends State<LocationScreen> {
  GoogleMapController? _mapController;
  LatLng _currentLocation = const LatLng(28.7041, 77.1025); // Default location
  Set<Marker> _markers = {};
  String _landmark = "Fetching location...";

  @override
  void initState() {
    super.initState();
    _startLocationUpdates();
  }

  void _startLocationUpdates() {
    LocationService().getLocationStream().listen((location) {
      if (mounted) {
        setState(() {
          _currentLocation = location;
          _markers = {
            Marker(
              markerId: const MarkerId('current'),
              position: _currentLocation,
              icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueRed),
              infoWindow: const InfoWindow(title: "Your Location"),
            )
          };
        });

        _updateLandmark(_currentLocation);
        _mapController?.animateCamera(CameraUpdate.newLatLng(_currentLocation));
      }
    });
  }

  Future<void> _updateLandmark(LatLng position) async {
    try {
      List<Placemark> placemarks = await placemarkFromCoordinates(position.latitude, position.longitude);
      if (placemarks.isNotEmpty) {
        Placemark place = placemarks.first;
        setState(() {
          _landmark = "${place.name}, ${place.locality}, ${place.administrativeArea}";
        });
      } else {
        setState(() {
          _landmark = "Unknown location";
        });
      }
    } catch (e) {
      setState(() {
        _landmark = "Error fetching landmark";
      });
      print("Error getting location name: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    double screenHeight = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: AppBar(title: const Text('Live Location')),
      body: Column(
        children: [
          SizedBox(
            height: screenHeight * 0.7,
            child: GoogleMap(
              initialCameraPosition: CameraPosition(target: _currentLocation, zoom: 15),
              markers: _markers,
              onMapCreated: (GoogleMapController controller) {
                _mapController = controller;
              },
              myLocationEnabled: true,
              myLocationButtonEnabled: true,
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
              child: ListView(
                // crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text("Current Location:", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8),
                  Text("Latitude: ${_currentLocation.latitude}", style: const TextStyle(fontSize: 16)),
                  Text("Longitude: ${_currentLocation.longitude}", style: const TextStyle(fontSize: 16)),
                  const SizedBox(height: 8),
                  Text("Landmark: $_landmark", style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
