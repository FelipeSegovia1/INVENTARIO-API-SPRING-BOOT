package com.inventario.productos.controllers;

import com.inventario.productos.dto.ProductoDTO;
import com.inventario.productos.services.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;  
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/inventario/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;
    

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    @GetMapping("/hateoas/{id}")
    public ProductoDTO obtenerHATEOAS (@PathVariable Integer id){
        ProductoDTO dto = service.obtenerPorId(id);
        dto.add(linkTo(methodOn(InventarioControlador.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(InventarioControlador.class).obtenerTodosHateoas()).withRel("Todos los productos"));

        return dto;
    }

    

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id, @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}