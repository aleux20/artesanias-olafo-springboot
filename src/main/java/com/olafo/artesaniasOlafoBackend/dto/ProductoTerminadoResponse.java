package com.olafo.artesaniasOlafoBackend.dto;

import com.olafo.artesaniasOlafoBackend.model.ProductoTerminado;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Datos de salida de un producto terminado, incluyendo el nombre de su
 * categoría.
 */
public record ProductoTerminadoResponse(
        Integer idProducto,
        Integer idCategoria,
        String nombreCategoria,
        String nombre,
        String descripcion,
        BigDecimal precioVenta,
        Integer stockActual,
        Boolean activo,
        LocalDate fechaCreacion) {

    /** Convierte la entidad en su representación de respuesta. */
    public static ProductoTerminadoResponse desde(ProductoTerminado p) {
        return new ProductoTerminadoResponse(
                p.getIdProducto(),
                p.getCategoria().getIdCategoria(),
                p.getCategoria().getNombre(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecioVenta(),
                p.getStockActual(),
                p.getActivo(),
                p.getFechaCreacion());
    }
}
