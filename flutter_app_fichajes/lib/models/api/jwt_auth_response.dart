import 'package:json_annotation/json_annotation.dart';
import 'user_response.dart';

part 'jwt_auth_response.g.dart';

@JsonSerializable()
class JwtAuthResponse {
  final String accessToken;
  final String tokenType;
  final UserResponse userResponseDTO;

  JwtAuthResponse({
    required this.accessToken,
    required this.tokenType,
    required this.userResponseDTO,
  });

  factory JwtAuthResponse.fromJson(Map<String, dynamic> json) =>
      _$JwtAuthResponseFromJson(json);

  Map<String, dynamic> toJson() => _$JwtAuthResponseToJson(this);
}
