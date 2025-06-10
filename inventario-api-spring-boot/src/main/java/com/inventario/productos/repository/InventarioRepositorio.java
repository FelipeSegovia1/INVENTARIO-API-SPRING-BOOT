package com.inventario.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventario.productos.models.Inventario;

@Repository
public interface InventarioRepositorio extends JpaRepository<Inventario, Integer> { 

}
