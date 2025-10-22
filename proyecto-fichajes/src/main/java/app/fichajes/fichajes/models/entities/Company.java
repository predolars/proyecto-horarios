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
    @NotBlank(message = "The field cannot be null or empty")
    private String cif;

    @Column(nullable = false)
    @NotBlank(message = "The field cannot be null or empty")
    private String address;

    @NotNull(message = "The field cannot be null or empty")
    private Double latitude;

    @NotNull(message = "The field cannot be null or empty")
    private Double longitude;

    @Column(name = "company_name", nullable = false, unique = true)
    @NotBlank(message = "The field cannot be null or empty")
    private String companyName;

    // 1,N relationship with assignments table
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

}