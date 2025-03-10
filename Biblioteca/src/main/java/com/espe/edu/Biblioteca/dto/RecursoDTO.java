package com.espe.edu.Biblioteca.dto;

import lombok.Data;

@Data
public class RecursoDTO {
    private String id;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private String tipo;
    private String categoriaNombre; // 🔥 Ahora usamos el nombre en vez de categoriaId
    private Boolean disponible;
    private String imagenUrl; // Nuevo campo para la imagen
}
