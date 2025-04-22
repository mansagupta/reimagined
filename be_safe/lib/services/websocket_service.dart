import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/status.dart' as status;

class WebSocketService {
  final String _serverUrl = "ws://localhost:8083/api/ws";
  late IOWebSocketChannel _channel;

  void connect() {
    try {
      _channel = IOWebSocketChannel.connect(_serverUrl);
      print("WebSocket Connected");

      _channel.stream.listen(
            (message) {
          print("Received message: $message");

        },
        onError: (error) {
          print("WebSocket error: $error");
        },
        onDone: () {
          print("WebSocket closed");
        },
      );
    } catch (e) {
      print("WebSocket connection failed: $e");
    }
  }

  void sendLocation(double lat, double lng) {
    try {
      final message = "{\"latitude\": $lat, \"longitude\": $lng}";
      _channel.sink.add(message);
      print("Sent location: $message");
    } catch (e) {
      print("Error sending location: $e");
    }
  }

  void disconnect() {
    _channel.sink.close(status.goingAway);
    print("WebSocket Disconnected");
  }
}
