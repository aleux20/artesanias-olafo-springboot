package com.olafo.artesaniasOlafoBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada para crear o actualizar una categoría.
 * Las anotaciones definen las validaciones básicas.
 */
public record CategoriaProductoRequest(
                @NotBlank(message = "El nombre es obligatorio") @Size(max = 50, message = "El nombre no puede superar 50 caracteres") String nombre,

                @Size(max = 255, message = "La descripción no puede superar 255 caracteres") String descripcion) {
}
