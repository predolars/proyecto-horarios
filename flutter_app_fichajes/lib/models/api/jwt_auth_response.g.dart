// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'jwt_auth_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

JwtAuthResponse _$JwtAuthResponseFromJson(Map<String, dynamic> json) =>
    JwtAuthResponse(
      accessToken: json['accessToken'] as String,
      tokenType: json['tokenType'] as String,
      userResponseDTO: UserResponse.fromJson(
        json['userResponseDTO'] as Map<String, dynamic>,
      ),
    );

Map<String, dynamic> _$JwtAuthResponseToJson(JwtAuthResponse instance) =>
    <String, dynamic>{
      'accessToken': instance.accessToken,
      'tokenType': instance.tokenType,
      'userResponseDTO': instance.userResponseDTO,
    };
