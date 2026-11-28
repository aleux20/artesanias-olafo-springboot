package com.olafo.artesaniasOlafoBackend.service;

import com.olafo.artesaniasOlafoBackend.dto.CategoriaProductoRequest;
import com.olafo.artesaniasOlafoBackend.dto.CategoriaProductoResponse;

import java.util.List;

/**
 * Contrato del módulo Categoría de producto.
 * Define las operaciones de negocio disponibles; la lógica está en
 * CategoriaServiceImpl.
 * Los controladores dependen de esta interfaz, no de la implementación.
 */
public interface CategoriaProductoService {
    /** Lista todas las categorías. */
    List<CategoriaProductoResponse> listar();

    /** Consulta una categoría por su id (404 si no existe). */
    CategoriaProductoResponse obtener(Integer id);

    /** Crea una categoría; el nombre no puede estar repetido. */
    CategoriaProductoResponse crear(CategoriaProductoRequest datos);

    /** Actualiza nombre y descripción de una categoría existente. */
    CategoriaProductoResponse actualizar(Integer id, CategoriaProductoRequest datos);

    /** Elimina una categoría, siempre que no tenga productos asociados. */
    void eliminar(Integer id);
}
