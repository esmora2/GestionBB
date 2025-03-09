package com.espe.edu.User.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol; // 🔥 Nuevo campo para el rol

    private boolean isVerified = false;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro; // 🆕 Nuevo campo para la fecha de registro

    @Column(nullable = true, length = 255)
    private String direccion; // 🆕 Nuevo campo para la dirección

    public Usuario() {}

    public Usuario(String email, String password, String nombre, Rol rol) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.isVerified = false;
        this.fechaRegistro = fechaRegistro;
        this.direccion = direccion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
    public boolean isVerified() { return isVerified; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public String getDireccion() { return direccion; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRol(Rol rol) { this.rol = rol; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
