package com.inventario.productos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventario.productos.dto.AjusteInventarioDTO;
import com.inventario.productos.dto.InventarioDTO;
import com.inventario.productos.dto.MovimientoInventarioDTO;
import com.inventario.productos.models.Inventario;
import com.inventario.productos.services.InventarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


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

    @PutMapping("/hateoas/ajuste")
    public ResponseEntity<InventarioDTO> ajustarStock(@RequestBody AjusteInventarioDTO dto){
        Inventario inventario = service.ajustarStock(dto);

        InventarioDTO dtohateoas = convertirAInventarioDTO(inventario);

        dtohateoas.add(linkTo(methodOn(InventarioControlador.class).ObtenerPorId(inventario.getIdInventario())).withSelfRel());
        dtohateoas.add(linkTo(methodOn(InventarioControlador.class).listarInventario()).withRel("Productos ajustados"));
        
    return ResponseEntity.ok(dtohateoas);
    }


    private InventarioDTO convertirAInventarioDTO(Inventario inventario) {
        throw new UnsupportedOperationException("Unimplemented method 'convertirAInventarioDTO'");
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

    @PostMapping("/hateoas/movimiento")
    public ResponseEntity<InventarioDTO> registrarMovimiento(@RequestBody MovimientoInventarioDTO dto){
        Inventario inventario = service.registrarMovimiento(dto);
        InventarioDTO inventarioDTO = convertirAInventarioDTO(inventario);
        
        inventarioDTO.add(linkTo(methodOn(InventarioControlador.class).ObtenerPorId(inventario.getIdInventario())).withSelfRel());
        inventarioDTO.add(linkTo(methodOn(InventarioControlador.class).listarInventario(inventario.getIdInventario())).withRel("Ver movimientos inventario"));
    
        return ResponseEntity.ok(inventarioDTO);
    }


    


    




private Class<?> listarInventario(Integer idInventario) {
        throw new UnsupportedOperationException("Unimplemented method 'listarInventario'");
    }
public Class<?> obtenerTodosHateoas() {
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodosHateoas'");
    }
    public Class<?> obtenerHATEOAS(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'obtenerHATEOAS'");
    }

}
    

