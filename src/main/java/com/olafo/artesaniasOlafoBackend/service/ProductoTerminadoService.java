package com.olafo.artesaniasOlafoBackend.service;

import com.olafo.artesaniasOlafoBackend.dto.ProductoTerminadoRequest;
import com.olafo.artesaniasOlafoBackend.dto.ProductoTerminadoResponse;

import java.util.List;

/**
 * Contrato del módulo Producto terminado.
 * Define las operaciones de negocio disponibles; la lógica está en
 * ProductoServiceImpl.
 */
public interface ProductoTerminadoService {
    /**
     * Lista productos con filtros opcionales.
     *
     * @param idCategoria si no es null, solo productos de esa categoría
     * @param soloActivos si es true, omite los productos inactivos
     */
    List<ProductoTerminadoResponse> listar(Integer idCategoria, boolean soloActivos);

    /** Consulta un producto por su id (404 si no existe). */
    ProductoTerminadoResponse obtener(Integer id);

    /** Crea un producto nuevo (queda activo y con fecha de creación de hoy). */
    ProductoTerminadoResponse crear(ProductoTerminadoRequest datos);

    /** Actualiza los datos de un producto existente. */
    ProductoTerminadoResponse actualizar(Integer id, ProductoTerminadoRequest datos);

    /** Eliminación lógica: marca el producto como inactivo (activo = 0). */
    void inactivar(Integer id);

    /** Reactiva un producto previamente inactivado. */
    ProductoTerminadoResponse reactivar(Integer id);
}
