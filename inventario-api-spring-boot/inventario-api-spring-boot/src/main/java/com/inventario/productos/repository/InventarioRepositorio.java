package com.inventario.productos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventario.productos.models.Inventario;

@Repository
public interface InventarioRepositorio extends JpaRepository<Inventario, Integer> { 

        Optional<Inventario> findByProducto_IdProducto(Integer idProducto);

}
