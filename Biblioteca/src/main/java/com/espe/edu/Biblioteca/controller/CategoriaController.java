package com.espe.edu.Biblioteca.controller;

import com.espe.edu.Biblioteca.dto.CategoriaDTO;
import com.espe.edu.Biblioteca.entity.Categoria;
import com.espe.edu.Biblioteca.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ✅ POST - Crear Categoría
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Categoria nuevaCategoria = categoriaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(nuevaCategoria));
    }

    // ✅ PUT - Editar Categoría por ID (Long)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> editar(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Categoria categoriaActualizada = categoriaService.editarPorId(id, dto);
        return ResponseEntity.ok(convertirADTO(categoriaActualizada));
    }

    // ✅ DELETE - Eliminar Categoría por ID (Long)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET - Listar Categorías (ADMIN y CLIENTE)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CLIENTE')")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar() {
        List<CategoriaDTO> categorias = categoriaService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    // ✅ Conversión de `Categoria` a `CategoriaDTO`
    private CategoriaDTO convertirADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId().toString());
        dto.setNombre(categoria.getNombre());
        return dto;
    }

    // ✅ Manejo de errores si la categoría no se encuentra
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
