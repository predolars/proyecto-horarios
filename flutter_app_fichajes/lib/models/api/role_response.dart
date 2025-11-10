import 'package:json_annotation/json_annotation.dart';

part 'role_response.g.dart';

@JsonSerializable()
class RoleResponse {
  final int id;
  final String roleName;

  RoleResponse({required this.id, required this.roleName});

  factory RoleResponse.fromJson(Map<String, dynamic> json) =>
      _$RoleResponseFromJson(json);

  Map<String, dynamic> toJson() => _$RoleResponseToJson(this);
}
