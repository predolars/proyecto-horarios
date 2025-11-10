import 'dart:io';

import 'package:flutter/foundation.dart';

class ApiConstants {
  static String get baseUrl {
    if (kIsWeb) {
      return 'http://localhost:8080/api/v1';
    } else if (Platform.isAndroid) {
      return 'http://10.0.2.2:8080/api/v1'; // IP especial del emulador Android
    } else {
      return 'http://localhost:8080/api/v1'; // Para iOS u otros
    }
  }

  static const String authLogin = '/auth/login';
}
