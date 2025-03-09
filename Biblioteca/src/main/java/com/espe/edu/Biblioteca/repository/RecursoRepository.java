package com.espe.edu.Biblioteca.repository;

import com.espe.edu.Biblioteca.entity.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    Optional<Recurso> findByTitulo(String titulo);
    List<Recurso> findByCategoriaId(Long categoriaId);
}
