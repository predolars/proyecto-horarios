import 'package:flutter/material.dart';
import 'package:flutter_app_fichajes/ui/widgets/assignment_card.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../providers/assginment_provider.dart';
import '../../providers/user_provider.dart';

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(currentUserProvider);
    final asyncAssignments = ref.watch(myAssignmentsProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text('Hola, ${user?.name ?? 'Usuario'}'),
        titleTextStyle: GoogleFonts.dmSans(fontSize: 30, color: Colors.black),
      ),
      body: Center(
        // 3. Usamos .when() para reaccionar al estado del FutureProvider
        child: asyncAssignments.when(
          // Estado de carga
          loading: () => const CircularProgressIndicator(),

          // Estado de error
          error: (err, stack) => Text('Error: $err'),

          // Estado de datos recibidos
          data: (assignments) {
            // Si la lista está vacía
            if (assignments.isEmpty) {
              return const Text('No tienes empresas asignadas.');
            }

            Text("Estos son tus puestos de trabajao:");

            // Si tenemos datos, mostramos la lista
            return ListView.builder(
              itemCount: assignments.length,
              itemBuilder: (context, index) {
                final assignment = assignments[index];
                return AssignmentCard(
                  assignment: assignment,
                  onTap: () {
                    print('Tapped on ${assignment.company.companyName}');
                  },
                );
              },
            );
          },
        ),
      ),
    );
  }
}
