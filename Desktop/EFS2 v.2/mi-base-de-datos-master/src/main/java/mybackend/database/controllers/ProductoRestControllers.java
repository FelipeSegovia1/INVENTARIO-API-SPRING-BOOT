package mybackend.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mybackend.database.entities.Producto;
import mybackend.database.services.ProductoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174","http://localhost:5175","http://localhost:5176","http://localhost:5177"})
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoRestControllers {

    @Autowired
    private ProductoServices productoServices;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoServices.crear(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoServices.listarTodas();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoServices.obtenerId(id);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        Producto producto = productoServices.actualizar(id, productoActualizado);
        return ResponseEntity.ok(producto);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Producto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(productoServices.desactivar(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar productos por nombre", description = "Busca productos que contengan el nombre especificado")
    public ResponseEntity<List<Producto>> buscarPorNombre(
            @Parameter(description = "Nombre o parte del nombre del producto") 
            @RequestParam String nombre) {
        List<Producto> productos = productoServices.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/byCategoria/{categoriaId}")
    @Operation(summary = "Buscar productos por categoría", description = "Obtiene todos los productos de una categoría específica")
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @Parameter(description = "ID de la categoría") 
            @PathVariable Long categoriaId) {
        List<Producto> productos = productoServices.buscarPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Productos con stock bajo", description = "Obtiene productos con stock menor a 5 unidades")
    public ResponseEntity<List<Producto>> obtenerProductosConStockBajo() {
        List<Producto> productos = productoServices.obtenerProductosConStockBajo();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar productos activos", description = "Obtiene todos los productos activos")
    public ResponseEntity<List<Producto>> listarProductosActivos() {
        List<Producto> productos = productoServices.listarProductosActivos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/search/activos")
    @Operation(summary = "Buscar productos activos por nombre", description = "Busca productos activos que contengan el nombre especificado")
    public ResponseEntity<List<Producto>> buscarPorNombreActivos(
            @Parameter(description = "Nombre o parte del nombre del producto") 
            @RequestParam String nombre) {
        List<Producto> productos = productoServices.buscarPorNombreActivos(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/byCategoria/{categoriaId}/activos")
    @Operation(summary = "Buscar productos activos por categoría", description = "Obtiene productos activos de una categoría específica")
    public ResponseEntity<List<Producto>> buscarPorCategoriaActivos(
            @Parameter(description = "ID de la categoría") 
            @PathVariable Long categoriaId) {
        List<Producto> productos = productoServices.buscarPorCategoriaActivos(categoriaId);
        return ResponseEntity.ok(productos);
    }
}
