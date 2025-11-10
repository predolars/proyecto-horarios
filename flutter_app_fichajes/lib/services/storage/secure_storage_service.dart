import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../models/api/user_response.dart';

// Este provider nos permitirá acceder al almacenamiento desde cualquier lado
final secureStorageProvider = Provider<SecureStorageService>(
  (ref) => SecureStorageService(),
);

class SecureStorageService {
  // Creamos la instancia una sola vez
  final _storage = const FlutterSecureStorage();

  static const _tokenKey = 'ACCESS_TOKEN';
  static const _userKey = 'USER_DATA';

  // --- TOKEN ---
  Future<void> saveToken(String token) async {
    await _storage.write(key: _tokenKey, value: token);
  }

  Future<String?> getToken() async {
    return await _storage.read(key: _tokenKey);
  }

  // --- USUARIO ---
  Future<void> saveUser(UserResponse user) async {
    // Convertimos el objeto a un String JSON
    final userJson = jsonEncode(user.toJson());
    await _storage.write(key: _userKey, value: userJson);
  }

  Future<UserResponse?> getUser() async {
    final userJson = await _storage.read(key: _userKey);
    if (userJson == null) return null;
    try {
      return UserResponse.fromJson(jsonDecode(userJson));
    } catch (e) {
      print('Error al parsear usuario guardado: $e');
      await deleteUser(); // Si está corrupto se borra
      return null;
    }
  }

  // --- CERRAR SESION (Vacio datos del usuario y borro el jwt token) ---
  Future<void> clearAll() async {
    await _storage.deleteAll();
  }

  Future<void> deleteUser() async => await _storage.delete(key: _userKey);
}
