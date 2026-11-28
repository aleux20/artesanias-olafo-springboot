package com.olafo.artesaniasOlafoBackend.service;

import com.olafo.artesaniasOlafoBackend.dto.CategoriaProductoRequest;
import com.olafo.artesaniasOlafoBackend.dto.CategoriaProductoResponse;
import com.olafo.artesaniasOlafoBackend.model.CategoriaProducto;
import com.olafo.artesaniasOlafoBackend.exception.RecursoNoEncontradoException;
import com.olafo.artesaniasOlafoBackend.exception.ReglaNegocioException;
import com.olafo.artesaniasOlafoBackend.repository.CategoriaProductoRepository;
import com.olafo.artesaniasOlafoBackend.repository.ProductoTerminadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de CategoriaService.
 * Reglas: el nombre es único y no se puede eliminar una categoría con
 * productos.
 */
@Service
@Transactional
public class CategoriaProductoServiceImpl implements CategoriaProductoService {
    private final CategoriaProductoRepository categoriaRepository;
    private final ProductoTerminadoRepository productoRepository;

    public CategoriaProductoServiceImpl(CategoriaProductoRepository categoriaRepository,
            ProductoTerminadoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaProductoResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaProductoResponse::desde)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaProductoResponse obtener(Integer id) {
        return CategoriaProductoResponse.desde(buscarEntidad(id));
    }

    @Override
    public CategoriaProductoResponse crear(CategoriaProductoRequest datos) {
        String nombre = datos.nombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre '" + nombre + "'");
        }
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setNombre(nombre);
        categoria.setDescripcion(datos.descripcion());
        return CategoriaProductoResponse.desde(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaProductoResponse actualizar(Integer id, CategoriaProductoRequest datos) {
        CategoriaProducto categoria = buscarEntidad(id);
        String nombre = datos.nombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCaseAndIdCategoriaNot(nombre, id)) {
            throw new ReglaNegocioException("Ya existe otra categoría con el nombre '" + nombre + "'");
        }
        categoria.setNombre(nombre);
        categoria.setDescripcion(datos.descripcion());
        return CategoriaProductoResponse.desde(categoriaRepository.save(categoria));
    }

    @Override
    public void eliminar(Integer id) {
        CategoriaProducto categoria = buscarEntidad(id);
        if (productoRepository.existsByCategoriaIdCategoria(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar la categoría '" + categoria.getNombre()
                            + "' porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
    }

    /** Busca la entidad o lanza 404 si no existe. */
    private CategoriaProducto buscarEntidad(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la categoría con id " + id));
    }
}
