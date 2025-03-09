package com.espe.edu.ec.Prestamo.repository;

import com.espe.edu.ec.Prestamo.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuarioEmail(String usuarioEmail);
}