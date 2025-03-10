package com.espe.edu.Biblioteca.controller;

import com.espe.edu.Biblioteca.dto.RecursoDTO;
import com.espe.edu.Biblioteca.service.AzureBlobStorageService;
import com.espe.edu.Biblioteca.entity.Recurso;
import com.espe.edu.Biblioteca.service.RecursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.bind.annotation.RequestParam;
import com.espe.edu.Biblioteca.repository.RecursoRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recursos")
public class RecursoController {

    private final RecursoService recursoService;
    private final RecursoRepository recursoRepository;
    private final AzureBlobStorageService azureBlobStorageService;

    public RecursoController(RecursoService recursoService, RecursoRepository recursoRepository, AzureBlobStorageService azureBlobStorageService) {
        this.recursoService = recursoService;
        this.recursoRepository = recursoRepository;
        this.azureBlobStorageService = azureBlobStorageService;
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
    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listar() {
        List<RecursoDTO> recursos = recursoService.listar().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/upload/{idRecurso}")
    public ResponseEntity<String> subirImagen(@PathVariable Long idRecurso, @RequestParam("file") MultipartFile file) {
        try {
            // Buscar el recurso en la base de datos
            Recurso recurso = recursoRepository.findById(idRecurso)
                    .orElseThrow(() -> new RuntimeException("Recurso no encontrado con ID: " + idRecurso));

            // Subir imagen a Azure Storage
            String imageUrl = azureBlobStorageService.subirImagen(file);

            // Guardar URL en la base de datos
            recurso.setImagenUrl(imageUrl);
            recursoRepository.save(recurso);

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen: " + e.getMessage());
        }
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
        dto.setImagenUrl(recurso.getImagenUrl()); // ✅ Incluir la imagen en el DTO
        return dto;
    }
}
