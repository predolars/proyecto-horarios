// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'exception_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ExceptionResponse _$ExceptionResponseFromJson(Map<String, dynamic> json) =>
    ExceptionResponse(
      status: (json['status'] as num).toInt(),
      error: json['error'] as String,
      message: json['message'] as String,
      timestamp: DateTime.parse(json['timestamp'] as String),
      fieldErrors: (json['fieldErrors'] as Map<String, dynamic>?)?.map(
        (k, e) => MapEntry(k, e as String),
      ),
    );

Map<String, dynamic> _$ExceptionResponseToJson(ExceptionResponse instance) =>
    <String, dynamic>{
      'status': instance.status,
      'error': instance.error,
      'message': instance.message,
      'timestamp': instance.timestamp.toIso8601String(),
      'fieldErrors': instance.fieldErrors,
    };
