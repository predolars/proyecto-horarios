package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tiendas")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccion;

    private Double latitud;
    private Double longitud;

    @Column(name = "margen_permitido_metros")
    private Integer margenPermitidoMetros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToMany(mappedBy = "tiendasAsignadas")
    private List<Trabajador> trabajadoresAsignados;

    @Column(name = "registro_activo")
    private Boolean registroActivo = true;

}