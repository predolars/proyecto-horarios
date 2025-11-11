import 'package:flutter/material.dart';

import '../../models/api/assignment_response.dart';

class AssignmentCard extends StatelessWidget {
  final AssignmentResponse assignment;
  final VoidCallback onTap;

  const AssignmentCard({
    super.key,
    required this.assignment,
    required this.onTap,
  });

  // Función para formatear la fecha (ej: 25/10/2025)
  String _formatDate(DateTime date) {
    final day = date.day.toString().padLeft(2, '0');
    final month = date.month.toString().padLeft(2, '0');
    final year = date.year;
    return "$day/$month/$year";
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
        padding: const EdgeInsets.all(20.0), // Más espaciado interior
        decoration: BoxDecoration(
          color: Colors.white, // Fondo blanco
          borderRadius: BorderRadius.circular(24.0), // Bordes muy redondeados
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withOpacity(0.1), // Sombra muy sutil
              spreadRadius: 2,
              blurRadius: 10,
              offset: const Offset(0, 5), // Sombra hacia abajo
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 1. Nombre de la Empresa
            Text(
              assignment.company.companyName,
              style: const TextStyle(
                fontSize: 22.0,
                fontWeight: FontWeight.bold,
                color: Colors.black87,
              ),
            ),
            const SizedBox(height: 8.0),

            // 2. Rol
            Text(
              assignment.role.roleName,
              style: TextStyle(fontSize: 16.0, color: Colors.grey.shade600),
            ),

            const SizedBox(height: 16.0),

            // 3. Fecha de Asignación
            Text(
              'Fecha de alta: ${_formatDate(assignment.assignmentDate)}',
              style: TextStyle(
                fontSize: 14.0,
                color: Colors.grey.shade500,
                fontStyle: FontStyle.italic,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
