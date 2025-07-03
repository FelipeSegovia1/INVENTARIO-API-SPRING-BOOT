package com.inventario.productos.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AjusteInventarioDTO  extends RepresentationModel<AjusteInventarioDTO>{
    private Integer idProducto;
    private Integer nuevoStock;

}