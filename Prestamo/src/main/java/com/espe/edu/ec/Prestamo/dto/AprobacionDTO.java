package com.espe.edu.ec.Prestamo.dto;

import lombok.Data;

@Data
public class AprobacionDTO {
    private Long prestamoId;
    private boolean aprobado;

    // Getters y Setters
    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }
}
