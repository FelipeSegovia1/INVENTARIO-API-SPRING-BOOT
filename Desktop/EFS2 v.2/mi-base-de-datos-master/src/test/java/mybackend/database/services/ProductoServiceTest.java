package mybackend.database.services;

import mybackend.database.entities.Categoria;
import mybackend.database.entities.Producto;
import mybackend.database.repositories.ProductoRepositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepositories productoRepositories;

    @InjectMocks
    private ProductoServiceslmpl productoService;

    private Producto producto1;
    private Producto producto2;
    private Producto productoStockBajo;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Ropa interior");

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Producto Test 1");
        producto1.setDescripcion("Descripción test 1");
        producto1.setPrecio(new BigDecimal("29.90"));
        producto1.setStock(10);
        producto1.setImagenUrl("https://example.com/imagen1.jpg");
        producto1.setActivo(true);
        producto1.setCategoria(categoria);
        producto1.setFechaCreacion(Instant.now());

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Producto Test 2");
        producto2.setDescripcion("Descripción test 2");
        producto2.setPrecio(new BigDecimal("49.90"));
        producto2.setStock(15);
        producto2.setImagenUrl("https://example.com/imagen2.jpg");
        producto2.setActivo(true);
        producto2.setCategoria(categoria);
        producto2.setFechaCreacion(Instant.now());

        productoStockBajo = new Producto();
        productoStockBajo.setId(3L);
        productoStockBajo.setNombre("Producto Stock Bajo");
        productoStockBajo.setDescripcion("Producto con stock bajo");
        productoStockBajo.setPrecio(new BigDecimal("19.90"));
        productoStockBajo.setStock(2);
        productoStockBajo.setImagenUrl("https://example.com/imagen3.jpg");
        productoStockBajo.setActivo(true);
        productoStockBajo.setCategoria(categoria);
        productoStockBajo.setFechaCreacion(Instant.now());
    }

    @Test
    void testObtenerId_Exitoso() {
        // Arrange
        when(productoRepositories.findById(anyLong())).thenReturn(Optional.of(producto1));

        // Act
        Producto resultado = productoService.obtenerId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Producto Test 1", resultado.getNombre());
        assertEquals(new BigDecimal("29.90"), resultado.getPrecio());
        assertEquals(10, resultado.getStock());

        verify(productoRepositories).findById(1L);
    }

    @Test
    void testObtenerId_ProductoNoEncontrado() {
        // Arrange
        when(productoRepositories.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> productoService.obtenerId(999L));
        
        assertEquals("Producto no encontrado ", exception.getMessage());
        
        verify(productoRepositories).findById(999L);
    }

    @Test
    void testObtenerProductosConStockBajo() {
        // Arrange
        List<Producto> productosStockBajo = Arrays.asList(productoStockBajo);
        when(productoRepositories.findProductosConStockBajo()).thenReturn(productosStockBajo);

        // Act
        List<Producto> resultado = productoService.obtenerProductosConStockBajo();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Producto Stock Bajo", resultado.get(0).getNombre());
        assertEquals(2, resultado.get(0).getStock());
        assertTrue(resultado.get(0).isLowStock());

        verify(productoRepositories).findProductosConStockBajo();
    }

    @Test
    void testBuscarPorNombre_Exitoso() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto1);
        when(productoRepositories.findByNombreContainingIgnoreCase(anyString())).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorNombre("Test");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Producto Test 1", resultado.get(0).getNombre());

        verify(productoRepositories).findByNombreContainingIgnoreCase("Test");
    }

    @Test
    void testBuscarPorNombre_NombreVacio() {
        // Act
        List<Producto> resultado = productoService.buscarPorNombre("");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(productoRepositories, never()).findByNombreContainingIgnoreCase(anyString());
    }

    @Test
    void testBuscarPorNombre_NombreNull() {
        // Act
        List<Producto> resultado = productoService.buscarPorNombre(null);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(productoRepositories, never()).findByNombreContainingIgnoreCase(anyString());
    }

    @Test
    void testBuscarPorCategoria_Exitoso() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoRepositories.findByCategoriaId(anyLong())).thenReturn(productos);

        // Act
        List<Producto> resultado = productoService.buscarPorCategoria(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto Test 1", resultado.get(0).getNombre());
        assertEquals("Producto Test 2", resultado.get(1).getNombre());

        verify(productoRepositories).findByCategoriaId(1L);
    }

    @Test
    void testBuscarPorCategoria_CategoriaIdNull() {
        // Act
        List<Producto> resultado = productoService.buscarPorCategoria(null);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(productoRepositories, never()).findByCategoriaId(anyLong());
    }

    @Test
    void testListarProductosActivos() {
        // Arrange
        List<Producto> productosActivos = Arrays.asList(producto1, producto2);
        when(productoRepositories.findByActivoTrue()).thenReturn(productosActivos);

        // Act
        List<Producto> resultado = productoService.listarProductosActivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.get(0).isActivo());
        assertTrue(resultado.get(1).isActivo());

        verify(productoRepositories).findByActivoTrue();
    }

    @Test
    void testCrear_Exitoso() {
        // Arrange
        when(productoRepositories.save(any(Producto.class))).thenReturn(producto1);

        // Act
        Producto resultado = productoService.crear(producto1);

        // Assert
        assertNotNull(resultado);
        assertEquals("Producto Test 1", resultado.getNombre());
        assertEquals(new BigDecimal("29.90"), resultado.getPrecio());

        verify(productoRepositories).save(producto1);
    }
}