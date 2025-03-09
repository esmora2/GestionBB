package com.espe.edu.ec.Prestamo.service;

import com.espe.edu.ec.Prestamo.client.BibliotecaClient;
import com.espe.edu.ec.Prestamo.dto.AprobacionDTO;
import com.espe.edu.ec.Prestamo.dto.PrestamoDTO;
import com.espe.edu.ec.Prestamo.entity.Prestamo;
import com.espe.edu.ec.Prestamo.repository.PrestamoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final BibliotecaClient bibliotecaClient;
    private final HttpServletRequest request; // 🔥 Inyectamos el request para obtener el token

    public PrestamoService(PrestamoRepository prestamoRepository, BibliotecaClient bibliotecaClient, HttpServletRequest request) {
        this.prestamoRepository = prestamoRepository;
        this.bibliotecaClient = bibliotecaClient;
        this.request = request;
    }

    public Prestamo solicitarPrestamo(PrestamoDTO dto) {
        String usuarioEmail = obtenerEmailUsuarioAutenticado();

        // 🔥 Convertir el código de copia a Long
        Long idCopia;
        try {
            idCopia = Long.parseLong(dto.getCodigoCopia());
        } catch (NumberFormatException e) {
            throw new RuntimeException("El ID de la copia debe ser un número válido.");
        }

        // 🔥 Verificar disponibilidad en el Microservicio de Biblioteca
        boolean disponible = bibliotecaClient.verificarDisponibilidad(idCopia, request.getHeader("Authorization")).getBody();

        if (!disponible) {
            throw new RuntimeException("La copia no está disponible para préstamo");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setCodigoCopia(dto.getCodigoCopia());
        prestamo.setUsuarioEmail(usuarioEmail);
        prestamo.setFechaSolicitud(LocalDate.now());
        prestamo.setEstado("PENDIENTE");

        return prestamoRepository.save(prestamo);
    }


    // ✅ Aprobar o rechazar préstamo (Solo Administradores)
    public Prestamo aprobarORechazarPrestamo(AprobacionDTO dto) {
        Prestamo prestamo = prestamoRepository.findById(dto.getPrestamoId()) // 🔥 Ahora usa Long
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if ("APROBADO".equals(prestamo.getEstado()) || "RECHAZADO".equals(prestamo.getEstado())) {
            throw new RuntimeException("El préstamo ya fue procesado");
        }

        prestamo.setEstado(dto.isAprobado() ? "APROBADO" : "RECHAZADO");
        if (dto.isAprobado()) {
            prestamo.setFechaDevolucion(LocalDate.now().plusDays(14)); // 🔥 14 días para devolver
        }

        return prestamoRepository.save(prestamo);
    }


    // ✅ Ver historial del usuario autenticado (Clientes)
    public List<Prestamo> obtenerHistorialUsuario() {
        String usuarioEmail = obtenerEmailUsuarioAutenticado();
        return prestamoRepository.findByUsuarioEmail(usuarioEmail);
    }

    // ✅ Ver historial de todos los préstamos (Administradores)
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return prestamoRepository.findAll();
    }

    // 🔥 Obtener email del usuario autenticado desde el token
    private String obtenerEmailUsuarioAutenticado() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getTokenAttributes().get("sub").toString();
    }

    // 🔥 Método para obtener el rol del usuario autenticado
    private String obtenerRolUsuarioAutenticado() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Object rolObj = authentication.getTokenAttributes().get("rol");

        if (rolObj == null) {
            throw new RuntimeException("No se encontró el rol en el token.");
        }

        return rolObj.toString();
    }
}
