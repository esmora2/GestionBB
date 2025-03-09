package com.espe.edu.Biblioteca.service;

import com.espe.edu.Biblioteca.dto.RecursoDTO;
import com.espe.edu.Biblioteca.entity.Categoria;
import com.espe.edu.Biblioteca.entity.Recurso;
import com.espe.edu.Biblioteca.repository.CategoriaRepository;
import com.espe.edu.Biblioteca.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final CategoriaRepository categoriaRepository;

    public RecursoService(RecursoRepository recursoRepository, CategoriaRepository categoriaRepository) {
        this.recursoRepository = recursoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ✅ Crear recurso basado en el nombre de la categoría
    public Recurso crear(RecursoDTO dto) {
        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoriaNombre())
                .orElseThrow(() -> new RuntimeException("Categoría '" + dto.getCategoriaNombre() + "' no encontrada"));

        Recurso recurso = new Recurso();
        recurso.setTitulo(dto.getTitulo());
        recurso.setAutor(dto.getAutor());
        recurso.setEditorial(dto.getEditorial());
        recurso.setAnioPublicacion(dto.getAnioPublicacion());
        recurso.setTipo(dto.getTipo());
        recurso.setCategoria(categoria);
        recurso.setDisponible(dto.getDisponible());

        return recursoRepository.save(recurso);
    }

    // ✅ Editar recurso basado en su ID (Solo Administrador)
    public Recurso editar(Long id, RecursoDTO dto) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso con ID " + id + " no encontrado"));

        Categoria categoria = categoriaRepository.findByNombre(dto.getCategoriaNombre())
                .orElseThrow(() -> new RuntimeException("Categoría '" + dto.getCategoriaNombre() + "' no encontrada"));

        recurso.setTitulo(dto.getTitulo());
        recurso.setAutor(dto.getAutor());
        recurso.setEditorial(dto.getEditorial());
        recurso.setAnioPublicacion(dto.getAnioPublicacion());
        recurso.setTipo(dto.getTipo());
        recurso.setCategoria(categoria);
        recurso.setDisponible(dto.getDisponible());

        return recursoRepository.save(recurso);
    }

    // ✅ Eliminar recurso basado en su ID (Solo Administrador)
    public void eliminar(Long id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso con ID " + id + " no encontrado"));

        recursoRepository.delete(recurso);
    }

    // ✅ Listar recursos
    public List<Recurso> listar() {
        return recursoRepository.findAll();
    }
}
