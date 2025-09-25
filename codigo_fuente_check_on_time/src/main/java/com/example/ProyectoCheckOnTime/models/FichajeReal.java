package com.example.ProyectoCheckOnTime.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fichaje_real")
public class FichajeReal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_entrada_real", nullable = false)
    private LocalDateTime fechaHoraEntradaReal;

    @Column(name = "fecha_hora_salida_real")
    private LocalDateTime fechaHoraSalidaReal;

    @Column(name = "latitud_fichaje")
    private Double latitudFichaje;

    @Column(name = "longitud_fichaje")
    private Double longitudFichaje;

    @Column(name = "esta_en_zona")
    private Boolean estaEnZona;

    @Column(name = "justificacion_fuera_zona", columnDefinition = "TEXT")
    private String justificacionFueraZona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    public FichajeReal() {
    }

    public FichajeReal(Long id, LocalDateTime fechaHoraEntradaReal, LocalDateTime fechaHoraSalidaReal, Double latitudFichaje, Double longitudFichaje, Boolean estaEnZona, String justificacionFueraZona, Empresa empresa, Trabajador trabajador) {
        this.id = id;
        this.fechaHoraEntradaReal = fechaHoraEntradaReal;
        this.fechaHoraSalidaReal = fechaHoraSalidaReal;
        this.latitudFichaje = latitudFichaje;
        this.longitudFichaje = longitudFichaje;
        this.estaEnZona = estaEnZona;
        this.justificacionFueraZona = justificacionFueraZona;
        this.empresa = empresa;
        this.trabajador = trabajador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraEntradaReal() {
        return fechaHoraEntradaReal;
    }

    public void setFechaHoraEntradaReal(LocalDateTime fechaHoraEntradaReal) {
        this.fechaHoraEntradaReal = fechaHoraEntradaReal;
    }

    public LocalDateTime getFechaHoraSalidaReal() {
        return fechaHoraSalidaReal;
    }

    public void setFechaHoraSalidaReal(LocalDateTime fechaHoraSalidaReal) {
        this.fechaHoraSalidaReal = fechaHoraSalidaReal;
    }

    public Double getLatitudFichaje() {
        return latitudFichaje;
    }

    public void setLatitudFichaje(Double latitudFichaje) {
        this.latitudFichaje = latitudFichaje;
    }

    public Double getLongitudFichaje() {
        return longitudFichaje;
    }

    public void setLongitudFichaje(Double longitudFichaje) {
        this.longitudFichaje = longitudFichaje;
    }

    public Boolean getEstaEnZona() {
        return estaEnZona;
    }

    public void setEstaEnZona(Boolean estaEnZona) {
        this.estaEnZona = estaEnZona;
    }

    public String getJustificacionFueraZona() {
        return justificacionFueraZona;
    }

    public void setJustificacionFueraZona(String justificacionFueraZona) {
        this.justificacionFueraZona = justificacionFueraZona;
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