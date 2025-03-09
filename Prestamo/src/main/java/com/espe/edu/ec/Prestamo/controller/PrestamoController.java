package com.espe.edu.ec.Prestamo.controller;

import com.espe.edu.ec.Prestamo.dto.AprobacionDTO;
import com.espe.edu.ec.Prestamo.dto.PrestamoDTO;
import com.espe.edu.ec.Prestamo.entity.Prestamo;
import com.espe.edu.ec.Prestamo.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // ✅ Solicitar préstamo (Solo Clientes)
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarPrestamo(@RequestBody PrestamoDTO dto) {
        try {
            Prestamo prestamo = prestamoService.solicitarPrestamo(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Aprobar o rechazar préstamo (Solo Administradores)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/aprobar")
    public ResponseEntity<?> aprobarPrestamo(@RequestBody AprobacionDTO dto) {
        try {
            Prestamo prestamo = prestamoService.aprobarORechazarPrestamo(dto);
            return ResponseEntity.ok(prestamo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Historial de préstamos del usuario autenticado (Clientes)
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/historial")
    public ResponseEntity<List<Prestamo>> historialUsuario() {
        return ResponseEntity.ok(prestamoService.obtenerHistorialUsuario());
    }

    // ✅ Historial de todos los préstamos (Administradores)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/todos")
    public ResponseEntity<List<Prestamo>> todosLosPrestamos() {
        return ResponseEntity.ok(prestamoService.obtenerTodosLosPrestamos());
    }

    // ✅ Manejo de acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }
}
