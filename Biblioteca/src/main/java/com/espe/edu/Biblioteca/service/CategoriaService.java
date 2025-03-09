package com.espe.edu.Biblioteca.service;

import com.espe.edu.Biblioteca.dto.CategoriaDTO;
import com.espe.edu.Biblioteca.entity.Categoria;
import com.espe.edu.Biblioteca.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    // ✅ Crear una nueva categoría
    public Categoria crear(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        return repository.save(categoria);
    }

    // ✅ Editar una categoría por su ID (Long)
    public Categoria editarPorId(Long id, CategoriaDTO dto) {
        Categoria existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        existente.setNombre(dto.getNombre());
        return repository.save(existente);
    }

    // ✅ Eliminar una categoría por su ID (Long)
    public void eliminarPorId(Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        repository.delete(categoria);
    }

    // ✅ Listar todas las categorías
    public List<Categoria> listar() {
        return repository.findAll();
    }
}
