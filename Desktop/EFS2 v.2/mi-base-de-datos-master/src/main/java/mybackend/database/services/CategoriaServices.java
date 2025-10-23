package mybackend.database.services;

import java.util.List;

import mybackend.database.entities.Categoria;

public interface CategoriaServices {

    Categoria crear(Categoria categoria);
    Categoria obtenerId(Long id);
    List<Categoria> listarTodas();
    void eliminar(Long id);
    Categoria actualizar(Long id, Categoria categoriaActualizada);
}
