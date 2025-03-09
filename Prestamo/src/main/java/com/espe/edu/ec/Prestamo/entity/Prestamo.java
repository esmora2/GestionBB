package com.espe.edu.ec.Prestamo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prestamos")
@Data
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuarioEmail; // 🔥 Se extrae del token

    @Column(nullable = false)
    private String codigoCopia;

    @Column(nullable = false)
    private LocalDate fechaSolicitud;

    @Column(nullable = true)
    private LocalDate fechaDevolucion;

    @Column(nullable = false)
    private String estado; // PENDIENTE, APROBADO, RECHAZADO
}