package com.espe.edu.User.dto;

import com.espe.edu.User.entity.Rol;
import java.time.LocalDateTime;

public class UsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private boolean isVerified;
    private Rol rol; // 🔥 Nuevo campo para el rol
    private LocalDateTime fechaRegistro; // 🆕 Nuevo campo
    private String direccion; // 🆕 Nuevo campo

    public UsuarioResponse(Long id, String email, String nombre, boolean isVerified, Rol rol) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.isVerified = isVerified;
        this.rol = rol; // ✅ Asegurarse de incluir el rol
        this.fechaRegistro = fechaRegistro;
        this.direccion = direccion;
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public boolean isVerified() { return isVerified; }
    public Rol getRol() { return rol; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public String getDireccion() { return direccion; }
}
