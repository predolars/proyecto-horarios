// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'role_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RoleResponse _$RoleResponseFromJson(Map<String, dynamic> json) => RoleResponse(
  id: (json['id'] as num).toInt(),
  roleName: json['roleName'] as String,
);

Map<String, dynamic> _$RoleResponseToJson(RoleResponse instance) =>
    <String, dynamic>{'id': instance.id, 'roleName': instance.roleName};
