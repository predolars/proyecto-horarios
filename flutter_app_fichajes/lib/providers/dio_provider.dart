import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../config/api_constants.dart';
import '../services/storage/secure_storage_service.dart';

final dioProvider = Provider<Dio>((ref) {
  // Obtenemos acceso a nuestro storage seguro
  final storage = ref.watch(secureStorageProvider);

  // Configuración base: URL y tiempos de espera
  final dio = Dio(
    BaseOptions(
      baseUrl: ApiConstants.baseUrl,
      connectTimeout: const Duration(
        seconds: 15,
      ), // Si tarda más de 10s, da error
      receiveTimeout: const Duration(seconds: 15),
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
    ),
  );

  // AÑADIMOS LOS INTERCEPTORES (Los "porteros" de nuestras peticiones)
  dio.interceptors.add(
    InterceptorsWrapper(
      // 1. ANTES de enviar la petición
      onRequest: (options, handler) async {
        // Intentamos leer el token de la caja fuerte
        final token = await storage.getToken();

        // Si existe, lo metemos en la cabecera 'Authorization'
        if (token != null && token.isNotEmpty) {
          options.headers['Authorization'] = 'Bearer $token';
        }

        // ¡Adelante con la petición!
        return handler.next(options);
      },

      // 2. SI OCURRE UN ERROR (opcional por ahora, pero útil para debug)
      onError: (DioException e, handler) {
        // todo: Aquí podrías detectar si el error es 401 (token caducado) y redirigir al login
        return handler.next(e);
      },
    ),
  );

  return dio;
});
