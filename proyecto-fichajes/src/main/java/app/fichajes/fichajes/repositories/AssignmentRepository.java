package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.Role;
import app.fichajes.fichajes.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByUserAndCompanyAndRole(User user, Company company, Role role);

    List<Assignment> findByUser(User user);

}