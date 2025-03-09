package com.espe.edu.Biblioteca.controller;

import com.espe.edu.Biblioteca.dto.RecursoDTO;
import com.espe.edu.Biblioteca.entity.Recurso;
import com.espe.edu.Biblioteca.service.RecursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    // ✅ Crear recurso (Solo ADMIN)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RecursoDTO dto) {
        try {
            Recurso nuevoRecurso = recursoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(nuevoRecurso));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Editar recurso por ID (Solo Administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<RecursoDTO> editar(@PathVariable Long id, @RequestBody RecursoDTO dto) {
        Recurso recursoActualizado = recursoService.editar(id, dto);
        return ResponseEntity.ok(convertirADTO(recursoActualizado));
    }

    // ✅ Eliminar recurso por ID (Solo Administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Listar recursos (ADMIN y CLIENTE)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CLIENTE')")
    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listar() {
        List<RecursoDTO> recursos = recursoService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    // ✅ Manejo de error de acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }

    // ✅ Método para convertir `Recurso` a `RecursoDTO`
    private RecursoDTO convertirADTO(Recurso recurso) {
        RecursoDTO dto = new RecursoDTO();
        dto.setId(recurso.getId().toString());
        dto.setTitulo(recurso.getTitulo());
        dto.setAutor(recurso.getAutor());
        dto.setEditorial(recurso.getEditorial());
        dto.setAnioPublicacion(recurso.getAnioPublicacion());
        dto.setTipo(recurso.getTipo());
        dto.setCategoriaNombre(recurso.getCategoria().getNombre());
        dto.setDisponible(recurso.isDisponible());
        return dto;
    }
}
