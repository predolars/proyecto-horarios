package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asignaciones")
public class Asignacion {
    @Id
    private Long id;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

    // Relacion N,1 con tabla usuarios
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresaId;

    // Relacion N,1 con tabla usuarios
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioId;

    // Relacion N,1 con tabla roles
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private Rol rolId;

    @Column(name = "registro_activo")
    private Boolean registroActivo = true;

}
