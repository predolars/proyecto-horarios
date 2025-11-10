// Provider para acceder a este servicio
import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../config/api_constants.dart';
import '../models/api/jwt_auth_response.dart';
import '../models/api/login_request.dart';
import '../providers/dio_provider.dart';
import '../providers/user_provider.dart';
import 'storage/secure_storage_service.dart';

final authServiceProvider = Provider<AuthService>((riverpodProvider) {
  return AuthService(riverpodProvider);
});

class AuthService {
  final Ref riverpodProvider;

  AuthService(this.riverpodProvider);

  Future<bool> login(String email, String password) async {
    try {
      final dio = riverpodProvider.read(dioProvider);
      final storage = riverpodProvider.read(secureStorageProvider);

      // 1. Preparamos la petición
      final request = LoginRequest(username: email, password: password);

      // 2. Hacemos POST a /auth/login
      final response = await dio.post(
        ApiConstants.authLogin,
        data: request.toJson(),
      );

      // 3. Parseamos la respuesta
      final authResponse = JwtAuthResponse.fromJson(response.data);

      // 4. Guardar Token y Usuario en DISCO (Persistencia)
      await storage.saveUser(authResponse.userResponseDTO);

      // 5. Actualizar el estado en MEMORIA (para que la UI reaccione)
      riverpodProvider.read(currentUserProvider.notifier).state =
          authResponse.userResponseDTO;

      return true;
    } on DioException catch (e) {
      // Manejo básico de errores para probar
      if (e.response?.statusCode == 401) {
        print('❌ Credenciales incorrectas');
      } else {
        print('❌ Error de conexión: ${e.message}');
      }
      return false;
    } catch (e) {
      print('❌ Error inesperado: $e');
      return false;
    }
  }

  Future<void> logout() async {
    final storage = riverpodProvider.read(secureStorageProvider);
    await storage.clearAll(); // Borra token y usuario del disco
    riverpodProvider.read(currentUserProvider.notifier).state =
        null; // Borra usuario de la memoria
  }
}
