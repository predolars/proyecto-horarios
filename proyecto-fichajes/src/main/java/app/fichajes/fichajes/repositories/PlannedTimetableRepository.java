package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.PlannedTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedTimetableRepository extends JpaRepository<PlannedTimetable, Long> {
}