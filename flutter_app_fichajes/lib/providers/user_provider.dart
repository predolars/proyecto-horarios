import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/api/user_response.dart';

// Este provider guardará el usuario actual en memoria RAM.
// Puede ser null si nadie ha iniciado sesión.
final currentUserProvider = StateProvider<UserResponse?>((ref) => null);
