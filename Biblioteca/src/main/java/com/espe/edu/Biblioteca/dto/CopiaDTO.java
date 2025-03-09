package com.espe.edu.Biblioteca.dto;

import lombok.Data;

@Data
public class CopiaDTO {
    private String id;
    private String codigoCopia;
    private String estado;
    private String recursoNombre; // 🔥 Ahora lo guardamos también en la base de datos
}
