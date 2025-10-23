package mybackend.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mybackend.database.entities.Categoria;
import mybackend.database.repositories.CategoriaRepositories;

@Service
public class CategoriaServiceslmpl implements CategoriaServices {

    @Autowired
    private CategoriaRepositories categoriaRepositories;

    @Override
    public Categoria crear(Categoria categoria) {
        return categoriaRepositories.save(categoria);
    }

    @Override
    public Categoria obtenerId(Long id) {
        return categoriaRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada "));
    }

    @Override
    public List<Categoria> listarTodas() {
        return (List<Categoria>) categoriaRepositories.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!categoriaRepositories.existsById(id)) {
            throw new RuntimeException("Categoria no encontrada");
        }
        categoriaRepositories.deleteById(id);
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoriaActualizada) {
        Categoria existente = obtenerId(id);
        existente.setNombre(categoriaActualizada.getNombre());
        return categoriaRepositories.save(existente);
    }
}
