package com.example.ProyectoCheckOnTime.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "trabajadores")
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    @Column(unique = true, length = 20)
    private String dni;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horarios;

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichajeReal> fichajes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trabajador_tienda", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_trabajador"), // Columna que referencia a esta entidad (Trabajador)
            inverseJoinColumns = @JoinColumn(name = "id_tienda") // Columna que referencia a la otra entidad (Tienda)
    )
    private List<Tienda> tiendasAsignadas;

    public Trabajador() {
    }

    public Trabajador(Long id, String nombre, String apellidos, String dni, String email, String password, Empresa empresa, Rol rol, List<Horario> horarios, List<FichajeReal> fichajes, List<Tienda> tiendasAsignadas) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.empresa = empresa;
        this.rol = rol;
        this.horarios = horarios;
        this.fichajes = fichajes;
        this.tiendasAsignadas = tiendasAsignadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public List<FichajeReal> getFichajes() {
        return fichajes;
    }

    public void setFichajes(List<FichajeReal> fichajes) {
        this.fichajes = fichajes;
    }

    public List<Tienda> getTiendasAsignadas() {
        return tiendasAsignadas;
    }

    public void setTiendasAsignadas(List<Tienda> tiendasAsignadas) {
        this.tiendasAsignadas = tiendasAsignadas;
    }

}