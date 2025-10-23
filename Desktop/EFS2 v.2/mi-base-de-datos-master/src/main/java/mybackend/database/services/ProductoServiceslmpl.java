package mybackend.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mybackend.database.entities.Producto;
import mybackend.database.repositories.ProductoRepositories;

@Service
public class ProductoServiceslmpl implements ProductoServices {

    @Autowired
    private ProductoRepositories productoRepositories;

    @Override
    public Producto crear(Producto producto){
        return productoRepositories.save(producto);
    }
    
    @Override
    public Producto obtenerId(Long id){
        return productoRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado "));
    }

    @Override
    public List<Producto> listarTodas(){
        return (List<Producto>) productoRepositories.findAll();
    }
    
    @Override
    public void eliminar(Long id){
        if (!productoRepositories.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepositories.deleteById(id);
    }
    
    @Override
    public Producto actualizar(Long id, Producto productoActualizado){
        Producto existente = obtenerId(id);
        
        // Actualizar todos los campos si no son nulos
        if (productoActualizado.getNombre() != null) {
            existente.setNombre(productoActualizado.getNombre());
        }
        if (productoActualizado.getDescripcion() != null) {
            existente.setDescripcion(productoActualizado.getDescripcion());
        }
        if (productoActualizado.getPrecio() != null) {
            existente.setPrecio(productoActualizado.getPrecio());
        }
        if (productoActualizado.getStock() != null) {
            existente.setStock(productoActualizado.getStock());
        }
        if (productoActualizado.getImagenUrl() != null) {
            existente.setImagenUrl(productoActualizado.getImagenUrl());
        }
        if (productoActualizado.getCategoria() != null) {
            existente.setCategoria(productoActualizado.getCategoria());
        }
        // El campo activo se puede actualizar explícitamente
        existente.setActivo(productoActualizado.isActivo());
        
        return productoRepositories.save(existente);
    }

    @Override
    public Producto desactivar(Long id) {
        Producto producto = obtenerId(id);
        producto.setActivo(false);
        return productoRepositories.save(producto);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return productoRepositories.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return List.of();
        }
        return productoRepositories.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepositories.findProductosConStockBajo();
    }

    @Override
    public List<Producto> listarProductosActivos() {
        return productoRepositories.findByActivoTrue();
    }

    @Override
    public List<Producto> buscarPorCategoriaActivos(Long categoriaId) {
        if (categoriaId == null) {
            return List.of();
        }
        return productoRepositories.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    @Override
    public List<Producto> buscarPorNombreActivos(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return productoRepositories.findByNombreContainingIgnoreCaseAndActivoTrue(nombre.trim());
    }
}
