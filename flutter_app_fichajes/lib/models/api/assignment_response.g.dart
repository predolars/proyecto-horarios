// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'assignment_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

AssignmentResponse _$AssignmentResponseFromJson(Map<String, dynamic> json) =>
    AssignmentResponse(
      id: (json['id'] as num).toInt(),
      assignmentDate: DateTime.parse(json['assignmentDate'] as String),
      user: UserResponse.fromJson(json['user'] as Map<String, dynamic>),
      company: CompanyResponse.fromJson(
        json['company'] as Map<String, dynamic>,
      ),
      role: RoleResponse.fromJson(json['role'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$AssignmentResponseToJson(AssignmentResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'assignmentDate': instance.assignmentDate.toIso8601String(),
      'user': instance.user,
      'company': instance.company,
      'role': instance.role,
    };
