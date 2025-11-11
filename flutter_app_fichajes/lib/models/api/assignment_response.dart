import 'package:json_annotation/json_annotation.dart';
import 'user_response.dart';
import 'company_response.dart';
import 'role_response.dart';

part 'assignment_response.g.dart';

@JsonSerializable()
class AssignmentResponse {
  final int id;
  final DateTime assignmentDate;
  final UserResponse user;
  final CompanyResponse company;
  final RoleResponse role;

  AssignmentResponse({
    required this.id,
    required this.assignmentDate,
    required this.user,
    required this.company,
    required this.role,
  });

  factory AssignmentResponse.fromJson(Map<String, dynamic> json) =>
      _$AssignmentResponseFromJson(json);

  Map<String, dynamic> toJson() => _$AssignmentResponseToJson(this);
}
