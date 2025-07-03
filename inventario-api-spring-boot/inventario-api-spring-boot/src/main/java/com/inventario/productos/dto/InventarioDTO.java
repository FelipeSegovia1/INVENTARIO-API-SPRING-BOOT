package com.inventario.productos.dto;
import org.springframework.hateoas.RepresentationModel;

import lombok.*;

@Data
@EqualsAndHashCode (callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO extends RepresentationModel<InventarioDTO>{
    private Integer idInventario;
    private Integer idProducto;
    private Integer stockDisponible;
    private String ubicacionBodega;

}
