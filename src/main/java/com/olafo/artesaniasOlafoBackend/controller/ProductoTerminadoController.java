package com.olafo.artesaniasOlafoBackend.controller;

import com.olafo.artesaniasOlafoBackend.dto.ProductoTerminadoRequest;
import com.olafo.artesaniasOlafoBackend.dto.ProductoTerminadoResponse;
import com.olafo.artesaniasOlafoBackend.service.ProductoTerminadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST del módulo Producto terminado.
 * Base: http://localhost:8080/api/productos
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoTerminadoController {
    private final ProductoTerminadoService servicio;

    public ProductoTerminadoController(ProductoTerminadoService servicio) {
        this.servicio = servicio;
    }

    /**
     * GET /api/productos : lista productos.
     * Filtros opcionales: ?idCategoria=1 y ?soloActivos=true
     */
    @GetMapping
    public List<ProductoTerminadoResponse> listar(
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(defaultValue = "false") boolean soloActivos) {
        return servicio.listar(idCategoria, soloActivos);
    }

    /** GET /api/productos/{id} : consulta un producto. */
    @GetMapping("/{id}")
    public ProductoTerminadoResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    /** POST /api/productos : crea un producto (responde 201 Created). */
    @PostMapping
    public ResponseEntity<ProductoTerminadoResponse> crear(@Valid @RequestBody ProductoTerminadoRequest datos) {
        ProductoTerminadoResponse creado = servicio.crear(datos);
        return ResponseEntity
                .created(URI.create("/api/productos/" + creado.idProducto()))
                .body(creado);
    }

    /** PUT /api/productos/{id} : actualiza un producto. */
    @PutMapping("/{id}")
    public ProductoTerminadoResponse actualizar(@PathVariable Integer id,
            @Valid @RequestBody ProductoTerminadoRequest datos) {
        return servicio.actualizar(id, datos);
    }

    /**
     * DELETE /api/productos/{id} : eliminación lógica, deja el producto inactivo
     * (204).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivar(@PathVariable Integer id) {
        servicio.inactivar(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/productos/{id}/activar : reactiva un producto inactivo. */
    @PatchMapping("/{id}/activar")
    public ProductoTerminadoResponse reactivar(@PathVariable Integer id) {
        return servicio.reactivar(id);
    }
}
