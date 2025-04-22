class SOS {
  final double latitude;
  final double longitude;
  final String username;

  SOS({required this.latitude, required this.longitude, required this.username});

  Map<String, dynamic> toJson() {
    return {
      'latitude': latitude,
      'longitude': longitude,
      'username':username
    };
  }
}
