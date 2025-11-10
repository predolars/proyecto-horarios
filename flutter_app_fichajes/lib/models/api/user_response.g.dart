// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserResponse _$UserResponseFromJson(Map<String, dynamic> json) => UserResponse(
  id: (json['id'] as num).toInt(),
  name: json['name'] as String,
  surnames: json['surnames'] as String,
  dni: json['dni'] as String?,
  email: json['email'] as String,
  phoneNumber: json['phoneNumber'] as String?,
);

Map<String, dynamic> _$UserResponseToJson(UserResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'surnames': instance.surnames,
      'dni': instance.dni,
      'email': instance.email,
      'phoneNumber': instance.phoneNumber,
    };
