package app.fichajes.fichajes.repositories;

import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.projection.CompanyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByCompanyName(String companyName);

    boolean existsByCif(String cif);

    Optional<Company> findByCompanyNameOrCifAndIdNot(String companyName, String cif, Long id);

    @Query("SELECT c.cif, c.companyName, c.address FROM Company c")
    Optional<CompanyProjection> query();
}
