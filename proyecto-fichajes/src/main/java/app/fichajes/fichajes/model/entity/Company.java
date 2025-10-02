package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cif", nullable = false, unique = true, length = 20)
    private String cif;

    @Column(nullable = false)
    private String address;

    private Double latitude;
    private Double longitude;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    // Relacion 1,N con tabla asignaciones
    @OneToMany(mappedBy = "company")
    private List<Assignment> assignments = new ArrayList<>();

}