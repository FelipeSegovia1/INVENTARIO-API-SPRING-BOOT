package com.inventario.productos.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.inventario.productos.models.Inventario;
import com.inventario.productos.models.Producto;
import com.inventario.productos.repository.InventarioRepositorio;
import com.inventario.productos.repository.ProductoRepository;
import com.inventario.productos.dto.InventarioDTO;

@Service
public class InventarioService {


    @Autowired
    private InventarioRepositorio inventarioRepositorio;
    @Autowired
    private ProductoRepository productoRepository;
    
    private InventarioDTO toDTO(Inventario inventario){
        InventarioDTO dto = new InventarioDTO();
        dto.setIdInventario(inventario.getIdInventario());
        dto.setIdProducto(inventario.getProducto().getIdProducto());
        dto.setStockDisponible(inventario.getStockDisponible());
        dto.setUbicacionBodega(inventario.getUbicacionBodega());
        return dto;
    }

    public Inventario toEntity(InventarioDTO dto){
        Inventario inventario = new Inventario();
        inventario.setIdInventario(dto.getIdInventario());
        //relacion Producto
        if(dto.getIdProducto() != null) {
            Producto producto = productoRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            inventario.setProducto(producto);
        }
        inventario.setStockDisponible(dto.getStockDisponible());
        inventario.setUbicacionBodega(dto.getUbicacionBodega());
        return inventario;        
    }

    public InventarioDTO crearInventario(InventarioDTO dto) {
        Inventario inventario = toEntity(dto);
        inventarioRepositorio.save(inventario);
        return toDTO(inventario);
    }
    public List<InventarioDTO> listarInventario() {
        return inventarioRepositorio.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public Optional<InventarioDTO> ObtenerPorId(Integer idInventario ){
        return inventarioRepositorio.findById(idInventario)
                .map(this::toDTO);
    }
    public Optional<InventarioDTO> actualizarInventario(Integer idInventario, InventarioDTO dto) {
        return inventarioRepositorio.findById(idInventario).map(inventario -> {
            if(dto.getIdProducto() != null) {
                Producto producto = productoRepository.findById(dto.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                inventario.setProducto(producto);
            }
            inventario.setStockDisponible(dto.getStockDisponible());
            inventario.setUbicacionBodega(dto.getUbicacionBodega());
            return toDTO(inventarioRepositorio.save(inventario));
        });
    }
    public boolean eliminarInventario(Integer idInventario) {
       if(inventarioRepositorio.existsById(idInventario)) {
            inventarioRepositorio.deleteById(idInventario);
            return true;
        }
        return false;
    }
}
