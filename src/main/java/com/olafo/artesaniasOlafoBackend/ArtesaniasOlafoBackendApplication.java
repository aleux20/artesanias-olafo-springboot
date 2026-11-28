package com.olafo.artesaniasOlafoBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del backend del Sistema de Gestión de Inventario
 * para Artesanías Olafo. Arranca el servidor embebido y escanea
 * todos los paquetes bajo com.olafo.
 */
@SpringBootApplication
public class ArtesaniasOlafoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtesaniasOlafoBackendApplication.class, args);
	}

}
