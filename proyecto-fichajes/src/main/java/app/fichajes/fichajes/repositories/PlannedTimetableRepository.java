package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.PlannedTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlannedTimetableRepository extends JpaRepository<PlannedTimetable, Long> {
    /**
     * Searches for planned schedules for a specific assignment that overlap with a given time range.
     * An overlap occurs if an existing schedule starts before the new one ends,
     * and ends after the new one begins.
     *
     * @param assignmentId  The ID of the employee's assignment.
     * @param startDateTime The start date and time of the new schedule to check.
     * @param endDateTime   The end date and time of the new schedule to check.
     * @return A list of planned schedules that overlap. If there are no overlaps, the list will be empty.
     */
    @Query("SELECT pt FROM PlannedTimetable pt WHERE pt.assignment.id = :assignmentId " +
            "AND pt.plannedDateTimeStart < :endDateTime AND pt.plannedDateTimeEnd > :startDateTime")
    List<PlannedTimetable> findOverlappingTimetables(Long assignmentId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT pt FROM PlannedTimetable pt WHERE pt.assignment.id = :assignmentId " +
            "AND pt.id <> :idToExclude " +
            "AND pt.plannedDateTimeStart < :endDateTime AND pt.plannedDateTimeEnd > :startDateTime")
    List<PlannedTimetable> findOverlappingTimetablesAndIdNot(Long assignmentId, LocalDateTime startDateTime, LocalDateTime endDateTime, Long idToExclude);

    List<PlannedTimetable> findAllByAssignment_Company(Company company);

    /**
     * Busca horarios para una asignación específica, filtrando opcionalmente por un rango de fechas.
     * Devuelve cualquier horario que se solape con el rango proporcionado.
     * (Se añade CAST(... AS timestamp) para compatibilidad con PostgreSQL cuando los parámetros son NULL)
     */
    @Query("SELECT pt FROM PlannedTimetable pt WHERE pt.assignment = :assignment " +
            "AND (CAST(:startDateTime AS timestamp) IS NULL OR pt.plannedDateTimeEnd > :startDateTime) " +
            "AND (CAST(:endDateTime AS timestamp) IS NULL OR pt.plannedDateTimeStart < :endDateTime) " +
            "ORDER BY pt.plannedDateTimeStart ASC")
    List<PlannedTimetable> findMyTimetablesFiltered(
            @Param("assignment") Assignment assignment,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

}