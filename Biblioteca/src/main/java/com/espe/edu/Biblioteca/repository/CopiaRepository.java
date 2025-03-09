package com.espe.edu.Biblioteca.repository;

import com.espe.edu.Biblioteca.entity.Copia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CopiaRepository extends JpaRepository<Copia, Long> {
    Optional<Copia> findByCodigoCopia(String codigoCopia);
}
