package com.olafo.artesaniasOlafoBackend.service;

import com.olafo.artesaniasOlafoBackend.dto.ProductoTerminadoRequest;
import com.olafo.artesaniasOlafoBackend.dto.ProductoTerminadoResponse;
import com.olafo.artesaniasOlafoBackend.model.CategoriaProducto;
import com.olafo.artesaniasOlafoBackend.model.ProductoTerminado;
import com.olafo.artesaniasOlafoBackend.exception.RecursoNoEncontradoException;
import com.olafo.artesaniasOlafoBackend.repository.CategoriaProductoRepository;
import com.olafo.artesaniasOlafoBackend.repository.ProductoTerminadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de ProductoService.
 * La eliminación es lógica: el producto se marca como inactivo (activo = 0)
 * para conservar el historial de movimientos de inventario.
 */
@Service
@Transactional
public class ProductoTerminadoServiceImpl implements ProductoTerminadoService {
    private final ProductoTerminadoRepository productoRepository;
    private final CategoriaProductoRepository categoriaRepository;

    public ProductoTerminadoServiceImpl(ProductoTerminadoRepository productoRepository,
            CategoriaProductoRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoTerminadoResponse> listar(Integer idCategoria, boolean soloActivos) {
        return productoRepository.buscar(idCategoria, soloActivos).stream()
                .map(ProductoTerminadoResponse::desde)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoTerminadoResponse obtener(Integer id) {
        return ProductoTerminadoResponse.desde(buscarEntidad(id));
    }

    @Override
    public ProductoTerminadoResponse crear(ProductoTerminadoRequest datos) {
        ProductoTerminado producto = new ProductoTerminado();
        copiarDatos(producto, datos);
        producto.setActivo(true);
        return ProductoTerminadoResponse.desde(productoRepository.save(producto));
    }

    @Override
    public ProductoTerminadoResponse actualizar(Integer id, ProductoTerminadoRequest datos) {
        ProductoTerminado producto = buscarEntidad(id);
        copiarDatos(producto, datos);
        return ProductoTerminadoResponse.desde(productoRepository.save(producto));
    }

    @Override
    public void inactivar(Integer id) {
        ProductoTerminado producto = buscarEntidad(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public ProductoTerminadoResponse reactivar(Integer id) {
        ProductoTerminado producto = buscarEntidad(id);
        producto.setActivo(true);
        return ProductoTerminadoResponse.desde(productoRepository.save(producto));
    }

    /**
     * Copia los datos del request a la entidad, verificando que la categoría
     * exista.
     */
    private void copiarDatos(ProductoTerminado producto, ProductoTerminadoRequest datos) {
        CategoriaProducto categoria = categoriaRepository.findById(datos.idCategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la categoría con id " + datos.idCategoria()));
        producto.setCategoria(categoria);
        producto.setNombre(datos.nombre().trim());
        producto.setDescripcion(datos.descripcion());
        producto.setPrecioVenta(datos.precioVenta());
        producto.setStockActual(datos.stockActual());
    }

    /** Busca la entidad o lanza 404 si no existe. */
    private ProductoTerminado buscarEntidad(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el producto con id " + id));
    }
}
