package com.olafo.artesaniasOlafoBackend.repository;

import com.olafo.artesaniasOlafoBackend.model.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de categorías. JpaRepository ya aporta el CRUD básico
 * (save, findById, findAll, deleteById...); aquí solo se declaran consultas
 * derivadas.
 */
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {
    /** ¿Existe una categoría con ese nombre (sin distinguir mayúsculas)? */
    boolean existsByNombreIgnoreCase(String nombre);

    /** Igual que el anterior, pero ignorando la categoría que se está editando. */
    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, Integer idCategoria);
}
