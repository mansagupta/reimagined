class User {
  final String username;
  final String email;
  final String password;
  final String mobile;

  User({required this.username, required this.email, required this.password, required this.mobile});

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      username: json['username'],
      email: json['email'],
      password: json['password'],
      mobile: json['mobile']
    );
  }
}
