package mybackend.database.repositories;

import org.springframework.data.repository.CrudRepository;

import mybackend.database.entities.Categoria;

public interface CategoriaRepositories extends CrudRepository<Categoria, Long> {

}
