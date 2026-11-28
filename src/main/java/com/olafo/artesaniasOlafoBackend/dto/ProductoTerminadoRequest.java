package com.olafo.artesaniasOlafoBackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Datos de entrada para crear o actualizar un producto terminado.
 * El stock inicial se define al crear; después cambia por movimientos de
 * inventario,
 * pero en este módulo también puede corregirse con la actualización.
 */
public record ProductoTerminadoRequest(

        @NotNull(message = "La categoría es obligatoria") Integer idCategoria,

        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre no puede superar 100 caracteres") String nombre,

        String descripcion,

        @NotNull(message = "El precio de venta es obligatorio") @DecimalMin(value = "0.00", message = "El precio de venta no puede ser negativo") @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales") BigDecimal precioVenta,

        @NotNull(message = "El stock actual es obligatorio") @PositiveOrZero(message = "El stock no puede ser negativo") Integer stockActual) {
}