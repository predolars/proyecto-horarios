package com.example.ProyectoCheckOnTime.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "cif", nullable = false, unique = true, length = 20)
    private String cif;

    @Column(name = "fecha_suscripcion")
    private LocalDate fechaSuscripcion;
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

    @Column(name = "estado_suscripcion", length = 50)
    private String estadoSuscripcion;

    public Empresa() {
    }

    public Empresa(String nombreEmpresa, String cif, LocalDate fechaSuscripcion, String estadoSuscripcion) {
        this.nombreEmpresa = nombreEmpresa;
        this.cif = cif;
        this.fechaSuscripcion = fechaSuscripcion;
        this.estadoSuscripcion = estadoSuscripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public LocalDate getFechaSuscripcion() {
        return fechaSuscripcion;
    }

    public void setFechaSuscripcion(LocalDate fechaSuscripcion) {
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public String getEstadoSuscripcion() {
        return estadoSuscripcion;
    }

    public void setEstadoSuscripcion(String estadoSuscripcion) {
        this.estadoSuscripcion = estadoSuscripcion;
    }

}