package com.espe.edu.ec.Prestamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // 🔥 Habilita la comunicación con el Microservicio de Biblioteca
public class PrestamoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrestamoApplication.class, args);
	}
}
