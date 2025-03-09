package com.espe.edu.Biblioteca.service;

import com.espe.edu.Biblioteca.dto.CopiaDTO;
import com.espe.edu.Biblioteca.entity.Copia;
import com.espe.edu.Biblioteca.entity.Recurso;
import com.espe.edu.Biblioteca.repository.CopiaRepository;
import com.espe.edu.Biblioteca.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CopiaService {

    private final CopiaRepository copiaRepository;
    private final RecursoRepository recursoRepository;

    public CopiaService(CopiaRepository copiaRepository, RecursoRepository recursoRepository) {
        this.copiaRepository = copiaRepository;
        this.recursoRepository = recursoRepository;
    }

    // ✅ Crear copia usando el nombre del recurso
    public Copia crear(CopiaDTO dto) {
        Recurso recurso = recursoRepository.findByTitulo(dto.getRecursoNombre())
                .orElseThrow(() -> new RuntimeException("Recurso '" + dto.getRecursoNombre() + "' no encontrado"));

        Copia copia = new Copia();
        copia.setCodigoCopia(dto.getCodigoCopia());
        copia.setEstado(dto.getEstado());
        copia.setRecurso(recurso);
        copia.setRecursoNombre(recurso.getTitulo()); // 🔥 Guardamos el nombre en la BD

        return copiaRepository.save(copia);
    }

    // ✅ Editar copia basado en su ID (Solo Administrador)
    public Copia editar(Long id, CopiaDTO dto) {
        Copia copia = copiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copia con ID " + id + " no encontrada"));

        Recurso recurso = recursoRepository.findByTitulo(dto.getRecursoNombre())
                .orElseThrow(() -> new RuntimeException("Recurso '" + dto.getRecursoNombre() + "' no encontrado"));

        copia.setCodigoCopia(dto.getCodigoCopia());
        copia.setEstado(dto.getEstado());
        copia.setRecurso(recurso);
        copia.setRecursoNombre(recurso.getTitulo());

        return copiaRepository.save(copia);
    }

    // ✅ Eliminar copia basado en su ID (Solo Administrador)
    public void eliminar(Long id) {
        Copia copia = copiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copia con ID " + id + " no encontrada"));

        copiaRepository.delete(copia);
    }

    // ✅ Verificar disponibilidad de la copia por ID
    public boolean verificarDisponibilidad(Long id) {
        return copiaRepository.findById(id)
                .map(copia -> "disponible".equalsIgnoreCase(copia.getEstado()))
                .orElse(false);
    }

    // ✅ Listar todas las copias
    public List<Copia> listar() {
        return copiaRepository.findAll();
    }
}
