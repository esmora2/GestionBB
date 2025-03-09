package com.espe.edu.Biblioteca.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "recursos")
@Data
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String autor;
    private String editorial;
    private int anioPublicacion;

    @Column(nullable = false)
    private String tipo; // libro, revista, ebook, audiolibro

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private boolean disponible = true;
}
