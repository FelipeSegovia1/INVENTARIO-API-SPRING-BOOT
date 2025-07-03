package com.inventario.productos.dto;
import org.springframework.hateoas.RepresentationModel;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO extends RepresentationModel<ProductoDTO> {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private Double precioUnitario;
    private String categoria;
    private Boolean activo;
}