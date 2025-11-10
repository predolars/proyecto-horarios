import 'package:json_annotation/json_annotation.dart';

part 'user_response.g.dart';

@JsonSerializable()
class UserResponse {
  final int id;
  final String name;
  final String surnames;
  final String? dni;
  final String email;
  final String? phoneNumber;

  UserResponse({
    required this.id,
    required this.name,
    required this.surnames,
    this.dni,
    required this.email,
    this.phoneNumber,
  });

  factory UserResponse.fromJson(Map<String, dynamic> json) =>
      _$UserResponseFromJson(json);

  Map<String, dynamic> toJson() => _$UserResponseToJson(this);
}
