package com.olafo.artesaniasOlafoBackend.dto;

import com.olafo.artesaniasOlafoBackend.model.CategoriaProducto;

/** Datos de salida de una categoría (lo que ve el cliente de la API). */
public record CategoriaProductoResponse(Integer idCategoria, String nombre, String descripcion) {
    /** Convierte la entidad en su representación de respuesta. */
    public static CategoriaProductoResponse desde(CategoriaProducto c) {
        return new CategoriaProductoResponse(c.getIdCategoria(), c.getNombre(), c.getDescripcion());
    }
}
