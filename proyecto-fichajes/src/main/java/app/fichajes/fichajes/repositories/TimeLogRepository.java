package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.TimeLog;
import app.fichajes.fichajes.models.enums.TimeLogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    Optional<TimeLog> findTopByAssignmentOrderByDateTimeTimelogDesc(Assignment assignment);

    /**
     * Busca TimeLogs para una asignación, con filtros opcionales.
     * Si un filtro es NULL, se ignora.
     */
    @Query("SELECT t FROM TimeLog t WHERE t.assignment = :assignment " +
            "AND (CAST(:startDateTime AS timestamp) IS NULL OR t.dateTimeTimelog >= :startDateTime) " +
            "AND (CAST(:endDateTime AS timestamp ) IS NULL OR t.dateTimeTimelog <= :endDateTime) " +
            "AND (:type IS NULL OR t.timeLogType = :type) " +
            "ORDER BY t.dateTimeTimelog DESC")
    List<TimeLog> findMyTimeLogsFiltered(
            @Param("assignment") Assignment assignment,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("type") TimeLogType type
    );
}