package com.olafo.artesaniasOlafoBackend.controller;

import com.olafo.artesaniasOlafoBackend.dto.CategoriaProductoRequest;
import com.olafo.artesaniasOlafoBackend.dto.CategoriaProductoResponse;
import com.olafo.artesaniasOlafoBackend.service.CategoriaProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST del módulo Categoría de producto.
 * Base: http://localhost:8080/api/categorias
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaProductoController {
    private final CategoriaProductoService servicio;

    public CategoriaProductoController(CategoriaProductoService servicio) {
        this.servicio = servicio;
    }

    /** GET /api/categorias : lista todas las categorías. */
    @GetMapping
    public List<CategoriaProductoResponse> listar() {
        return servicio.listar();
    }

    /** GET /api/categorias/{id} : consulta una categoría. */
    @GetMapping("/{id}")
    public CategoriaProductoResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    /** POST /api/categorias : crea una categoría (responde 201 Created). */
    @PostMapping
    public ResponseEntity<CategoriaProductoResponse> crear(@Valid @RequestBody CategoriaProductoRequest datos) {
        CategoriaProductoResponse creada = servicio.crear(datos);
        return ResponseEntity
                .created(URI.create("/api/categorias/" + creada.idCategoria()))
                .body(creada);
    }

    /** PUT /api/categorias/{id} : actualiza una categoría. */
    @PutMapping("/{id}")
    public CategoriaProductoResponse actualizar(@PathVariable Integer id,
            @Valid @RequestBody CategoriaProductoRequest datos) {
        return servicio.actualizar(id, datos);
    }

    /**
     * DELETE /api/categorias/{id} : elimina una categoría (responde 204 No
     * Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
