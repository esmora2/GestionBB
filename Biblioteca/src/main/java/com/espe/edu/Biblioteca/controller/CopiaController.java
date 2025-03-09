package com.espe.edu.Biblioteca.controller;

import com.espe.edu.Biblioteca.dto.CopiaDTO;
import com.espe.edu.Biblioteca.entity.Copia;
import com.espe.edu.Biblioteca.service.CopiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/copias")
public class CopiaController {

    private final CopiaService copiaService;

    public CopiaController(CopiaService copiaService) {
        this.copiaService = copiaService;
    }

    // ✅ Crear copia (Solo ADMIN)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CopiaDTO dto) {
        try {
            Copia nuevaCopia = copiaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(nuevaCopia));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Editar copia por ID (Solo Administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<CopiaDTO> editar(@PathVariable Long id, @RequestBody CopiaDTO dto) {
        Copia copiaActualizada = copiaService.editar(id, dto);
        return ResponseEntity.ok(convertirADTO(copiaActualizada));
    }

    // ✅ Eliminar copia por ID (Solo Administrador)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        copiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Listar todas las copias (ADMIN y CLIENTE)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CLIENTE')")
    @GetMapping
    public ResponseEntity<List<CopiaDTO>> listar() {
        List<CopiaDTO> copias = copiaService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(copias);
    }

    // ✅ Manejo de error de acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }

    // ✅ Método para convertir `Copia` a `CopiaDTO`
    private CopiaDTO convertirADTO(Copia copia) {
        CopiaDTO dto = new CopiaDTO();
        dto.setId(copia.getId().toString());
        dto.setCodigoCopia(copia.getCodigoCopia());
        dto.setEstado(copia.getEstado());
        dto.setRecursoNombre(copia.getRecursoNombre()); // 🔥 Ahora mostramos el nombre
        return dto;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CLIENTE')")
    @GetMapping("/{id}/disponible")
    public ResponseEntity<Boolean> verificarDisponibilidad(@PathVariable Long id) {
        boolean disponible = copiaService.verificarDisponibilidad(id);
        return ResponseEntity.ok(disponible);
    }
}
