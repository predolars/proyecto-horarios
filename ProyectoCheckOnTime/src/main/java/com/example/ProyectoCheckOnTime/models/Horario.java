package com.example.ProyectoCheckOnTime.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_entrada_planeada", nullable = false)
    private LocalDateTime fechaHoraEntradaPlaneada;

    @Column(name = "fecha_hora_salida_planeada", nullable = false)
    private LocalDateTime fechaHoraSalidaPlaneada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    public Horario() {
    }

    public Horario(Long id, LocalDateTime fechaHoraEntradaPlaneada, LocalDateTime fechaHoraSalidaPlaneada, Empresa empresa, Trabajador trabajador) {
        this.id = id;
        this.fechaHoraEntradaPlaneada = fechaHoraEntradaPlaneada;
        this.fechaHoraSalidaPlaneada = fechaHoraSalidaPlaneada;
        this.empresa = empresa;
        this.trabajador = trabajador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraEntradaPlaneada() {
        return fechaHoraEntradaPlaneada;
    }

    public void setFechaHoraEntradaPlaneada(LocalDateTime fechaHoraEntradaPlaneada) {
        this.fechaHoraEntradaPlaneada = fechaHoraEntradaPlaneada;
    }

    public LocalDateTime getFechaHoraSalidaPlaneada() {
        return fechaHoraSalidaPlaneada;
    }

    public void setFechaHoraSalidaPlaneada(LocalDateTime fechaHoraSalidaPlaneada) {
        this.fechaHoraSalidaPlaneada = fechaHoraSalidaPlaneada;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

}