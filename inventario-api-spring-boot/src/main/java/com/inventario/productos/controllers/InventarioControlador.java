package com.inventario.productos.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventario.productos.dto.InventarioDTO;
import com.inventario.productos.services.InventarioService;



@RestController
@RequestMapping("/api/inventario")
public class InventarioControlador {

    @Autowired
    private InventarioService service;

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> listarInventario() {
        return ResponseEntity.ok(service.listarInventario());
    }
    
    

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> ObtenerPorId(@PathVariable Integer id) {
        return service.ObtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } 

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> actualizarInventario(@PathVariable Integer id,@RequestBody InventarioDTO dto) {
        return service.actualizarInventario(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        
    
    }

    @PostMapping
    public  ResponseEntity<InventarioDTO> crearInventario(@RequestBody InventarioDTO dto) {
        return ResponseEntity.ok(service.crearInventario(dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Integer id) {
        return service.eliminarInventario(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}
