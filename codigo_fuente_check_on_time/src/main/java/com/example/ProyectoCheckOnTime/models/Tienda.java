package com.example.ProyectoCheckOnTime.models;

import jakarta.persistence.*;
import java.util.List;

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

    @ManyToMany(mappedBy = "tiendasAsignadas") // "mappedBy" apunta al nombre del campo en la clase Trabajador
    private List<Trabajador> trabajadoresAsignados;

    public Tienda() {
    }

    public Tienda(Long id, String direccion, Double latitud, Double longitud, Integer margenPermitidoMetros, Empresa empresa, List<Trabajador> trabajadoresAsignados) {
        this.id = id;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.margenPermitidoMetros = margenPermitidoMetros;
        this.empresa = empresa;
        this.trabajadoresAsignados = trabajadoresAsignados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Integer getMargenPermitidoMetros() {
        return margenPermitidoMetros;
    }

    public void setMargenPermitidoMetros(Integer margenPermitidoMetros) {
        this.margenPermitidoMetros = margenPermitidoMetros;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Trabajador> getTrabajadoresAsignados() {
        return trabajadoresAsignados;
    }

    public void setTrabajadoresAsignados(List<Trabajador> trabajadoresAsignados) {
        this.trabajadoresAsignados = trabajadoresAsignados;
    }

}