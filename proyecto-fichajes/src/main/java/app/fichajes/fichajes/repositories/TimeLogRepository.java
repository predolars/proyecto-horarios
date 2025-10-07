package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
}