package com.espe.edu.ec.Prestamo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "biblioteca-service", url = "http://localhost:8082")
public interface BibliotecaClient {

    @GetMapping("/copias/{id}/disponible") // 🔥 Ahora verifica por ID
    ResponseEntity<Boolean> verificarDisponibilidad(
            @PathVariable Long id,  // 🔥 Cambiado de String a Long
            @RequestHeader("Authorization") String token
    );
}
