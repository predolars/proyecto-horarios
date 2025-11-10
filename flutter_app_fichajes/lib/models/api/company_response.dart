import 'package:json_annotation/json_annotation.dart';

part 'company_response.g.dart';

@JsonSerializable()
class CompanyResponse {
  final int id;
  final String cif;
  final String address;
  final double latitude;
  final double longitude;
  final String companyName;

  CompanyResponse({
    required this.id,
    required this.cif,
    required this.address,
    required this.latitude,
    required this.longitude,
    required this.companyName,
  });

  factory CompanyResponse.fromJson(Map<String, dynamic> json) =>
      _$CompanyResponseFromJson(json);

  Map<String, dynamic> toJson() => _$CompanyResponseToJson(this);
}