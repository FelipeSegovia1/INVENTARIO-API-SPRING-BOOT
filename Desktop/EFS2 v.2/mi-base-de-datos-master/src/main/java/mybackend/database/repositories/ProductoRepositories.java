package mybackend.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mybackend.database.entities.Producto;
import java.util.List;

public interface ProductoRepositories extends JpaRepository<Producto, Long> {

    // Buscar productos por nombre (búsqueda parcial, insensible a mayúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos por categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    // Buscar productos activos
    List<Producto> findByActivoTrue();

    // Buscar productos con stock bajo (menos de 5)
    @Query("SELECT p FROM Producto p WHERE p.stock < 5")
    List<Producto> findProductosConStockBajo();

    // Buscar productos sin stock
    @Query("SELECT p FROM Producto p WHERE p.stock = 0")
    List<Producto> findProductosSinStock();

    // Buscar productos por categoría y activos
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    // Buscar productos por nombre y activos
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    // Contar productos por categoría
    long countByCategoriaId(Long categoriaId);

    // Buscar productos por rango de precio
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> findByPrecioBetween(@Param("precioMin") java.math.BigDecimal precioMin, 
                                      @Param("precioMax") java.math.BigDecimal precioMax);
}
