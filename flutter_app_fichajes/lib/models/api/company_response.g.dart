// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'company_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CompanyResponse _$CompanyResponseFromJson(Map<String, dynamic> json) =>
    CompanyResponse(
      id: (json['id'] as num).toInt(),
      cif: json['cif'] as String,
      address: json['address'] as String,
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      companyName: json['companyName'] as String,
    );

Map<String, dynamic> _$CompanyResponseToJson(CompanyResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'cif': instance.cif,
      'address': instance.address,
      'latitude': instance.latitude,
      'longitude': instance.longitude,
      'companyName': instance.companyName,
    };
