package app.fichajes.fichajes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cif", nullable = false, unique = true)
    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String cif;

    @Column(nullable = false)
    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String address;

    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private Double latitude;

    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private Double longitude;

    @Column(name = "company_name", nullable = false, unique = true)
    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String companyName;

    // Relacion 1,N con tabla asignaciones
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

}