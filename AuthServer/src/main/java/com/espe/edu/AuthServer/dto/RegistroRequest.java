    package com.espe.edu.AuthServer.dto;
    import java.time.LocalDateTime;
    
    public class RegistroRequest {
        private String nombre;
        private String email;
        private String password;
        private String rol; // Puede ser "ADMINISTRADOR" o "CLIENTE"
        private String direccion; // 🆕 Nuevo campo
        private LocalDateTime fechaRegistro; // 🆕 Nuevo campo

        public String getNombre() {
            return nombre;
        }
    
        public void setNombre(String nombre) {
            this.nombre = nombre;
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
    
        public String getRol() {
            return rol;
        }
    
        public void setRol(String rol) {
            this.rol = rol;
        }
        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public LocalDateTime getFechaRegistro() {
            return fechaRegistro;
        }

        public void setFechaRegistro(LocalDateTime fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
        }

    }
