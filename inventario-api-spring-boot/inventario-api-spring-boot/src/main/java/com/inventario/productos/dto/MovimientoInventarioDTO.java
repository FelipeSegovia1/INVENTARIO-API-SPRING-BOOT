package com.inventario.productos.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;

@Data
@EqualsAndHashCode (callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioDTO extends RepresentationModel<MovimientoInventarioDTO>{
    private Integer idProducto;
    private Integer cantidad;
    private String ubicacionBodega; 

}
