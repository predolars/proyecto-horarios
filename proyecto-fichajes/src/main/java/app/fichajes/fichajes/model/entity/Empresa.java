package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoSuscripcion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @Column(unique = true)
    private Long id;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "cif", nullable = false, unique = true, length = 20)
    private String cif;

    @Column(name = "fecha_suscripcion")
    private LocalDate fechaSuscripcion;

    @Column(name = "estado_suscripcion", length = 50)
    private EstadoSuscripcion estadoSuscripcion = EstadoSuscripcion.ACTIVA;

    @Column(name = "registro_activo")
    private Boolean registroActivo = true;

}