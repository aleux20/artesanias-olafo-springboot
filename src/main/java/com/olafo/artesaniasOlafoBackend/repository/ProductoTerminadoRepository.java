package com.olafo.artesaniasOlafoBackend.repository;

import com.olafo.artesaniasOlafoBackend.model.ProductoTerminado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** Acceso a datos de productos terminados. */
public interface ProductoTerminadoRepository extends JpaRepository<ProductoTerminado, Integer> {
    /** ¿La categoría tiene productos asociados? Se usa antes de eliminarla. */
    boolean existsByCategoriaIdCategoria(Integer idCategoria);

    /**
     * Lista productos con filtros opcionales por categoría y por estado activo.
     * Se usa JOIN FETCH para traer la categoría en la misma consulta.
     */
    @Query("""
            SELECT p FROM ProductoTerminado p JOIN FETCH p.categoria c
            WHERE (:idCategoria IS NULL OR c.idCategoria = :idCategoria)
              AND (:soloActivos = false OR p.activo = true)
            ORDER BY p.nombre
            """)
    List<ProductoTerminado> buscar(@Param("idCategoria") Integer idCategoria,
            @Param("soloActivos") boolean soloActivos);
}
