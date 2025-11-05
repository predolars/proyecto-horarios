package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.LeaveRequest;
import app.fichajes.fichajes.models.enums.LeaveRequestState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.assignment.company = :company AND lr.leaveRequestState = :state")
    List<LeaveRequest> findByCompanyAndState(
            @Param("company") Company company,
            @Param("state") LeaveRequestState state
    );

    /**
     * Busca solicitudes de ausencia para una asignación que se solapen con un rango de fechas.
     */
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.assignment = :assignment " +
            "AND lr.dateTimeStart < :endDate AND lr.dateTimeEnd > :startDate")
    List<LeaveRequest> findOverlappingRequests(
            @Param("assignment") Assignment assignment,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}