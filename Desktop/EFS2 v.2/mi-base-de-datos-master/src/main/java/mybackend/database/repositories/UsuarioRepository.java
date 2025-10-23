package mybackend.database.repositories;

import mybackend.database.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional<Usuario> el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado
     * @param email el email a verificar
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por rol
     * @param rol el rol a buscar
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol(String rol);

    /**
     * Busca usuarios por estado
     * @param estado el estado a buscar (activo/inactivo)
     * @return lista de usuarios con ese estado
     */
    List<Usuario> findByEstado(String estado);

    /**
     * Busca usuarios activos
     * @return lista de usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.estado = 'activo'")
    List<Usuario> findUsuariosActivos();

    /**
     * Busca usuarios por nombre (búsqueda parcial, case insensitive)
     * @param nombre el nombre o parte del nombre a buscar
     * @return lista de usuarios que coinciden
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Cuenta usuarios por rol
     * @param rol el rol a contar
     * @return número de usuarios con ese rol
     */
    long countByRol(String rol);

    /**
     * Busca usuarios por rol y estado
     * @param rol el rol
     * @param estado el estado
     * @return lista de usuarios que coinciden
     */
    List<Usuario> findByRolAndEstado(String rol, String estado);
}