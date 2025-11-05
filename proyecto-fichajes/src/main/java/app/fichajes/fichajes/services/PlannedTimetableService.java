package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.GenericException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.TimetableRequestDTO;
import app.fichajes.fichajes.models.dtos.response.PlannedTimetableResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.PlannedTimetable;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.PlannedTimetableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class PlannedTimetableService {

    private final PlannedTimetableRepository plannedTimetableRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    private static final String EXCEPTION_MESSAGE = "Assignment not found with id: ";

    @Autowired
    public PlannedTimetableService(PlannedTimetableRepository plannedTimetableRepository, AssignmentRepository assignmentRepository, ModelMapper modelMapper) {
        this.plannedTimetableRepository = plannedTimetableRepository;
        this.assignmentRepository = assignmentRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * This method saves the data to the database in batches.
     *
     * @param timetablesDTOs Requires a list of TimetableRequestDTO
     * @return A list of PlannedTimetableResponseDTO
     **/
    @Transactional
    public List<PlannedTimetableResponseDTO> createTimetables(List<TimetableRequestDTO> timetablesDTOs) {

        // 1. Validación de solapamientos en el lote
        // Antes de tocar la BBDD, comprobamos que los horarios del lote que nos llega no se solapen entre ellos mismos
        // Agrupamos por empleado (asignación):
        Map<Long, List<TimetableRequestDTO>> groupedByAssignment = timetablesDTOs.stream()
                .collect(Collectors.groupingBy(TimetableRequestDTO::getAssignmentId));

        // Y ahora, para cada empleado, validamos su lista de horarios nuevos
        groupedByAssignment.values().forEach(employeeTimetables -> {
            // Los ordenamos por fecha de inicio para poder compararlos en orden (N vs. N+1)
            employeeTimetables.sort(Comparator.comparing(TimetableRequestDTO::getPlannedDateTimeStart));

            // Iteramos en un rango de índices para poder comparar el horario actual con el siguiente
            IntStream.range(0, employeeTimetables.size()).forEach(timetableIndex -> {
                TimetableRequestDTO current = employeeTimetables.get(timetableIndex);
                // Validaciones de negocio básicas (fechas coherentes, no anteriores a hoy, etc.)
                validateTimetableDates(current);

                // Comprobamos que 'current' no se solape con 'next'
                if (timetableIndex + 1 < employeeTimetables.size()) {
                    TimetableRequestDTO next = employeeTimetables.get(timetableIndex + 1);
                    if (current.getPlannedDateTimeEnd().isAfter(next.getPlannedDateTimeStart())) {
                        throw new FieldDataAlreadyExistsException(
                                "Error in batch: There are overlapping schedules for assignment ID: " + current.getAssignmentId() + " at dates: " + next.getPlannedDateTimeStart()
                        );
                    }
                }

                // 2. Validación de solapamientos con la base de datos
                // Una vez sabemos que el horario actual('current') es válido dentro del lote,
                // comprobamos que no se solape con nada que ya exista en la BBDD
                if (!plannedTimetableRepository.findOverlappingTimetables(
                        current.getAssignmentId(),
                        current.getPlannedDateTimeStart(),
                        current.getPlannedDateTimeEnd()
                ).isEmpty()) {
                    throw new FieldDataAlreadyExistsException("The employee (ID: " + current.getAssignmentId() + ") already has an overlapping for that day: " + current.getPlannedDateTimeStart() + ".");
                }
            });
        });

        // 3. Recopilar todos los IDs necesarios
        // Recogemos TODOS los IDs (tanto del 'assignment' como del 'creador') para traerlos de la BBDD en una sola consulta
        Set<Long> assignmentIds = timetablesDTOs.stream()
                .flatMap(dto -> Stream.of(dto.getAssignmentId(), dto.getCreatedByAssignmentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 4. Obtener entidades Assignment en un Mapa para acceso rápido
        // Traemos los 'Assignment' y los metemos en un Mapa para poder acceder a ellos por ID sin tener que volver a consultar
        Map<Long, Assignment> assignmentMap = assignmentRepository.findAllById(assignmentIds).stream()
                .collect(Collectors.toMap(Assignment::getId, Function.identity()));

        // 5. Mapeo final de DTO a Entidad
        // Con todos los datos validados y las entidades cargadas en memoria, convertimos
        // los DTOs de entrada a entidades 'PlannedTimetable' listas para guardar
        List<PlannedTimetable> timetablesToSave = timetablesDTOs.stream()
                .map(dto -> {
                    // Verificamos que el Assignment principal existe en nuestro mapa
                    Assignment assignment = Optional.ofNullable(assignmentMap.get(dto.getAssignmentId()))
                            .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + dto.getAssignmentId()));

                    PlannedTimetable timetable = modelMapper.map(dto, PlannedTimetable.class);
                    timetable.setAssignment(assignment);

                    // Verificamos que el Assignment del creador, si no es nulo, también exista en el mapa
                    if (dto.getCreatedByAssignmentId() != null) {
                        Assignment createdBy = Optional.ofNullable(assignmentMap.get(dto.getCreatedByAssignmentId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Creator assignment not found with id: " + dto.getCreatedByAssignmentId()));
                        timetable.setCreateByAssignment(createdBy);
                    }

                    return timetable;
                }).toList();

        // 6. Guardado en lote y respuesta
        // Guardamos todas las entidades en una única transacción
        return plannedTimetableRepository.saveAll(timetablesToSave).stream()
                .map(savedTimetable -> modelMapper.map(savedTimetable, PlannedTimetableResponseDTO.class))
                .toList();
    }

    // Función para validar que las fechas del 'timetable' cumplan las validaciones:
    // La fecha de entrada debe ser anterior a la de salida y no se pueden crear 'timetables' anteriores a hoy
    private void validateTimetableDates(TimetableRequestDTO dto) {
        // La duración de un turno tiene que ser de mínimo 2 horas
        long totalMinutes = Duration.between(dto.getPlannedDateTimeStart(), dto.getPlannedDateTimeEnd()).toMinutes();
        if (totalMinutes < 120) {
            throw new GenericException("The lapse between start time and end time must be 2 hours (120 minutes) or more");
        }

        if (dto.getPlannedDateTimeStart().isAfter(dto.getPlannedDateTimeEnd()) || dto.getPlannedDateTimeStart().isEqual(dto.getPlannedDateTimeEnd())) {
            throw new GenericException("The start date must be before the end date for the schedule of assignment ID: " + dto.getAssignmentId());
        }

        if (dto.getPlannedDateTimeStart().isBefore(LocalDateTime.now())) {
            throw new GenericException("Schedules cannot be planned for past dates for assignment ID: " + dto.getAssignmentId());
        }
    }

    @Transactional(readOnly = true)
    public List<PlannedTimetableResponseDTO> getAll() {
        return plannedTimetableRepository.findAll().stream()
                .map(t -> modelMapper.map(t, PlannedTimetableResponseDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlannedTimetableResponseDTO> getTimetablesForAssignment(Long assignmentId,
                                                                        LocalDate startDate,
                                                                        LocalDate endDate) {

        // 1. OBTENER USUARIO AUTENTICADO
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 2. BUSCAR ASIGNACIÓN Y VALIDAR PERTENENCIA (Security Check)
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + assignmentId));

        if (!assignment.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new AccessDeniedException("You do not have permission to view timetables for this assignment.");
        }

        // 3. LÓGICA DE FILTRADO (Convertir LocalDate a LocalDateTime)
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay(); // Ej: 2025-10-30 -> 2025-10-30T00:00:00
        }

        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX); // Ej: 2025-10-30 -> 2025-10-30T23:59:59...
        }

        // Lógica para "dia concreto" (si solo manda startDate)
        if (startDate != null && endDate == null) {
            endDateTime = startDate.atTime(LocalTime.MAX);
        }

        // 4. LLAMAR AL REPOSITORIO
        List<PlannedTimetable> timetables = plannedTimetableRepository.findMyTimetablesFiltered(
                assignment, startDateTime, endDateTime
        );

        // 5. MAPEAR Y DEVOLVER
        return timetables.stream()
                .map(t -> modelMapper.map(t, PlannedTimetableResponseDTO.class))
                .toList();
    }

    @Transactional
    public PlannedTimetableResponseDTO updateTimetable(Long id, TimetableRequestDTO dto) {
        // 1. Validar las fechas del DTO (reusamos la lógica)
        validateTimetableDates(dto);

        // 2. Buscar el horario existente
        PlannedTimetable timetableDb = plannedTimetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlannedTimetable not found with id: " + id));

        // 3. Cargar las asignaciones (solo si son necesarias)
        // (Nota: Podríamos optimizar esto para que solo busque si los IDs han cambiado)
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + dto.getAssignmentId()));

        Assignment createdBy = assignmentRepository.findById(dto.getCreatedByAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Creator assignment not found with id: " + dto.getCreatedByAssignmentId()));

        // 4. Validar solapamiento (excluyéndonos a nosotros mismos)
        if (!plannedTimetableRepository.findOverlappingTimetablesAndIdNot(
                dto.getAssignmentId(),
                dto.getPlannedDateTimeStart(),
                dto.getPlannedDateTimeEnd(),
                id // <-- El ID que excluimos
        ).isEmpty()) {
            throw new FieldDataAlreadyExistsException("The employee (ID: " + dto.getAssignmentId() + ") already has an overlapping schedule for that day.");
        }

        // 5. Mapear y actualizar
        // Usamos ModelMapper para actualizar los campos de 'timetableDb' con los de 'dto'
        modelMapper.map(dto, timetableDb);

        // Asignamos las entidades cargadas
        timetableDb.setAssignment(assignment);
        timetableDb.setCreateByAssignment(createdBy);

        // 6. @Transactional guarda automáticamente. Devolvemos la respuesta.
        return modelMapper.map(timetableDb, PlannedTimetableResponseDTO.class);
    }

    @Transactional
    public void deleteTimetable(Long id) {
        if (!plannedTimetableRepository.existsById(id)) {
            throw new ResourceNotFoundException("PlannedTimetable not found with id: " + id);
        }
        plannedTimetableRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PlannedTimetableResponseDTO> getTimetablesForCompany(Long managerAssignmentId) {

        // 1. Obtener el usuario autenticado (Mánager)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // 2. Cargar la asignación del Mánager
        Assignment managerAssignment = assignmentRepository.findById(managerAssignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + managerAssignmentId));

        // 3. Comprobación de Seguridad (IDOR)
        // Verificamos que el 'assignmentId' pasado pertenece al usuario del token
        if (!managerAssignment.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new AccessDeniedException("You do not have permission to view all timetables.");
        }

        // 4. Obtener la compañía de esa asignación
        Company company = managerAssignment.getCompany();

        // 5. Llamar al nuevo metodo del repositorio
        List<PlannedTimetable> timetables = plannedTimetableRepository.findAllByAssignment_Company(company);

        // 6. Mapear y devolver
        return timetables.stream()
                .map(timetable -> modelMapper.map(timetable, PlannedTimetableResponseDTO.class))
                .toList();
    }
}