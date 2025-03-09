package com.espe.edu.Biblioteca.repository;

import com.espe.edu.Biblioteca.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);

}
