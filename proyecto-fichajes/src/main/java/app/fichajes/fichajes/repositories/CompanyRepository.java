package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCif(String cif);

    boolean existsByCompanyName(String companyName);
}
