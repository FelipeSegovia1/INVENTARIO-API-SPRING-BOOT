package mybackend.database.services;

import java.util.List;

import mybackend.database.entities.Producto;

public interface ProductoServices {

    Producto crear(Producto producto);
    Producto obtenerId(Long id);
    List<Producto> listarTodas();
    void eliminar(Long id);
    Producto actualizar(Long id, Producto productoActualizado);
    Producto desactivar(Long id);
    
    // Nuevos métodos de búsqueda
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorCategoria(Long categoriaId);
    List<Producto> obtenerProductosConStockBajo();
    List<Producto> listarProductosActivos();
    List<Producto> buscarPorCategoriaActivos(Long categoriaId);
    List<Producto> buscarPorNombreActivos(String nombre);
}
