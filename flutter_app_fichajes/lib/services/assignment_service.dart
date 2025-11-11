import 'package:dio/dio.dart';
import 'package:flutter_app_fichajes/config/api_constants.dart';
import 'package:flutter_app_fichajes/models/api/assignment_response.dart';
import 'package:flutter_app_fichajes/providers/dio_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final assignmentServiceProvider = Provider<AssignmentService>((ref) {
  return AssignmentService(ref);
});

class AssignmentService {
  final Ref _ref;
  late final Dio _dio;

  AssignmentService(this._ref) {
    _dio = _ref.read(dioProvider);
  }

  Future<List<AssignmentResponse>> getMyAssignments() async {
    try {
      final String endpoint =
          '${ApiConstants.baseUrl}${ApiConstants.assignments}/my';
      print('Ulr endpoint: $endpoint');

      final response = await _dio.get(endpoint);

      if (response.data is List) {
        final List<dynamic> jsonList = response.data;

        return jsonList
            .map((json) => AssignmentResponse.fromJson(json))
            .toList();
      } else {
        throw Exception('Respuesta inesperada del servidor');
      }
    } on DioException catch (e) {
      // Manejo de errores (podemos mejorarlo luego)
      print('Error al obtener asignaciones: ${e.message}');
      rethrow;
    } catch (e) {
      print('Error inesperado: $e');
      rethrow;
    }
  }
}
