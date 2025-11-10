import 'package:json_annotation/json_annotation.dart';

part 'exception_response.g.dart';

@JsonSerializable()
class ExceptionResponse {
  final int status;
  final String error;
  final String message;
  final DateTime timestamp;
  // Para los errores de validación 400
  final Map<String, String>? fieldErrors; 

  ExceptionResponse({
    required this.status,
    required this.error,
    required this.message,
    required this.timestamp,
    this.fieldErrors,
  });

  factory ExceptionResponse.fromJson(Map<String, dynamic> json) =>
      _$ExceptionResponseFromJson(json);

  Map<String, dynamic> toJson() => _$ExceptionResponseToJson(this);
}