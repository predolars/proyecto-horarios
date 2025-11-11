import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../models/api/assignment_response.dart';
import '../services/assignment_service.dart';

final myAssignmentsProvider = FutureProvider<List<AssignmentResponse>>((
  ref,
) async {
  // Obtenemos el servicio de asignaciones
  final assignmentService = ref.watch(assignmentServiceProvider);

  // Llamamos al método que creamos en el paso anterior
  return assignmentService.getMyAssignments();
});
