package com.espe.edu.Biblioteca.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "copias")
@Data
public class Copia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoCopia;

    @Column(nullable = false)
    private String estado; // disponible, prestado, reservado, dañado

    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;

    @Column(name = "recurso_nombre", nullable = false) // 🔥 Nuevo campo
    private String recursoNombre;
}
