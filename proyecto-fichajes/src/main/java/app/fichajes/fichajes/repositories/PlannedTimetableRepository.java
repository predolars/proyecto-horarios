package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.PlannedTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}