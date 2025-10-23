package mybackend.database.services;

import mybackend.database.entities.Usuario;
import mybackend.database.entities.UsuarioDTO;
import mybackend.database.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crear un nuevo usuario
     * @param usuario el usuario a crear
     * @return UsuarioDTO del usuario creado
     * @throws RuntimeException si el email ya existe
     */
    public UsuarioDTO crearUsuario(Usuario usuario) {
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asegurar estado activo por defecto
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("activo");
        }

        // Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuarioGuardado);
    }

    /**
     * Listar todos los usuarios
     * @return lista de UsuarioDTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Listar usuarios activos
     * @return lista de UsuarioDTO activos
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuariosActivos() {
        return usuarioRepository.findUsuariosActivos()
                .stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Buscar usuario por ID
     * @param id el ID del usuario
     * @return UsuarioDTO si existe
     * @throws RuntimeException si no existe
     */
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return new UsuarioDTO(usuario);
    }

    /**
     * Buscar usuario por email
     * @param email el email del usuario
     * @return UsuarioDTO si existe
     * @throws RuntimeException si no existe
     */
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return new UsuarioDTO(usuario);
    }

    /**
     * Actualizar usuario
     * @param id el ID del usuario a actualizar
     * @param usuarioActualizado los datos actualizados
     * @return UsuarioDTO actualizado
     * @throws RuntimeException si no existe o email duplicado
     */
    public UsuarioDTO actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar email único si se está cambiando
        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
                throw new RuntimeException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
            }
        }

        // Actualizar campos
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setRol(usuarioActualizado.getRol());
        usuarioExistente.setEstado(usuarioActualizado.getEstado());

        // Solo encriptar si se proporciona nueva contraseña
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);
        return new UsuarioDTO(usuarioGuardado);
    }

    /**
     * Inhabilitar usuario (cambiar estado a inactivo)
     * @param id el ID del usuario a inhabilitar
     * @return UsuarioDTO del usuario inhabilitado
     * @throws RuntimeException si no existe
     */
    public UsuarioDTO inhabilitarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEstado("inactivo");
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuarioGuardado);
    }

    /**
     * Habilitar usuario (cambiar estado a activo)
     * @param id el ID del usuario a habilitar
     * @return UsuarioDTO del usuario habilitado
     * @throws RuntimeException si no existe
     */
    public UsuarioDTO habilitarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEstado("activo");
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuarioGuardado);
    }

    /**
     * Eliminar usuario físicamente (solo para superadmin)
     * @param id el ID del usuario a eliminar
     * @throws RuntimeException si no existe
     */
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Buscar usuarios por rol
     * @param rol el rol a buscar
     * @return lista de UsuarioDTO con ese rol
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorRol(String rol) {
        return usuarioRepository.findByRol(rol)
                .stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Buscar usuarios por nombre (búsqueda parcial)
     * @param nombre el nombre o parte del nombre
     * @return lista de UsuarioDTO que coinciden
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Validar credenciales para login
     * @param email el email del usuario
     * @param password la contraseña en texto plano
     * @return UsuarioDTO si las credenciales son válidas
     * @throws RuntimeException si las credenciales son inválidas
     */
    @Transactional(readOnly = true)
    public UsuarioDTO validarCredenciales(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return new UsuarioDTO(usuario);
    }

    /**
     * Contar usuarios por rol
     * @param rol el rol a contar
     * @return número de usuarios con ese rol
     */
    @Transactional(readOnly = true)
    public long contarPorRol(String rol) {
        return usuarioRepository.countByRol(rol);
    }

    /**
     * Verificar si un usuario es superadmin
     * @param id el ID del usuario
     * @return true si es superadmin, false si no
     */
    @Transactional(readOnly = true)
    public boolean esSuperAdmin(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.isPresent() && usuario.get().isSuperAdmin();
    }
}