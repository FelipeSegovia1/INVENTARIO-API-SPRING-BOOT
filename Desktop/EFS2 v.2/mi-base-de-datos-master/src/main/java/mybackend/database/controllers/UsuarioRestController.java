package mybackend.database.controllers;

import mybackend.database.entities.Usuario;
import mybackend.database.entities.UsuarioDTO;
import mybackend.database.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174","http://localhost:5175","http://localhost:5176","http://localhost:5177"})
@Tag(name = "Usuarios", description = "Gestión de usuarios: CRUD y búsquedas")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Crear un nuevo usuario
     * POST /api/usuarios
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Listar todos los usuarios (solo superadmin)
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<?> listarUsuarios(@RequestParam(required = false) String adminId) {
        try {
            // Validación simple de superadmin
            if (adminId != null && !adminId.isEmpty()) {
                Long id = Long.parseLong(adminId);
                if (!usuarioService.esSuperAdmin(id)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin"));
                }
            }

            List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Listar usuarios activos
     * GET /api/usuarios/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<?> listarUsuariosActivos() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.listarUsuariosActivos();
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Buscar usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            UsuarioDTO usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar usuario por email
     * GET /api/usuarios/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            UsuarioDTO usuario = usuarioService.buscarPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar usuarios por rol (solo superadmin)
     * GET /api/usuarios/rol/{rol}
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<?> buscarPorRol(@PathVariable String rol, @RequestParam(required = false) String adminId) {
        try {
            // Validación simple de superadmin
            if (adminId != null && !adminId.isEmpty()) {
                Long id = Long.parseLong(adminId);
                if (!usuarioService.esSuperAdmin(id)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin"));
                }
            }

            List<UsuarioDTO> usuarios = usuarioService.buscarPorRol(rol);
            return ResponseEntity.ok(usuarios);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Buscar usuarios por nombre
     * GET /api/usuarios/search?nombre=...
     */
    @GetMapping("/search")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<UsuarioDTO> usuarios = usuarioService.buscarPorNombre(nombre);
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Actualizar usuario
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, 
                                             @Valid @RequestBody Usuario usuario,
                                             @RequestParam(required = false) String adminId) {
        try {
            // Si se está cambiando el rol, validar que sea superadmin
            if (usuario.getRol() != null && adminId != null && !adminId.isEmpty()) {
                Long adminIdLong = Long.parseLong(adminId);
                if (!usuarioService.esSuperAdmin(adminIdLong)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin para cambiar roles"));
                }
            }

            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Inhabilitar usuario (solo superadmin)
     * PUT /api/usuarios/{id}/inhabilitar
     */
    @PutMapping("/{id}/inhabilitar")
    public ResponseEntity<?> inhabilitarUsuario(@PathVariable Long id, @RequestParam String adminId) {
        try {
            Long adminIdLong = Long.parseLong(adminId);
            if (!usuarioService.esSuperAdmin(adminIdLong)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin"));
            }

            UsuarioDTO usuario = usuarioService.inhabilitarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Habilitar usuario (solo superadmin)
     * PUT /api/usuarios/{id}/habilitar
     */
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<?> habilitarUsuario(@PathVariable Long id, @RequestParam String adminId) {
        try {
            Long adminIdLong = Long.parseLong(adminId);
            if (!usuarioService.esSuperAdmin(adminIdLong)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin"));
            }

            UsuarioDTO usuario = usuarioService.habilitarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Eliminar usuario físicamente (solo superadmin)
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, @RequestParam String adminId) {
        try {
            Long adminIdLong = Long.parseLong(adminId);
            if (!usuarioService.esSuperAdmin(adminIdLong)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin"));
            }

            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(crearMensajeExito("Usuario eliminado correctamente"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    /**
     * Contar usuarios por rol (solo superadmin)
     * GET /api/usuarios/count/{rol}
     */
    @GetMapping("/count/{rol}")
    public ResponseEntity<?> contarPorRol(@PathVariable String rol, @RequestParam String adminId) {
        try {
            Long adminIdLong = Long.parseLong(adminId);
            if (!usuarioService.esSuperAdmin(adminIdLong)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearMensajeError("Acceso denegado. Se requiere rol de superadmin"));
            }

            long count = usuarioService.contarPorRol(rol);
            Map<String, Object> response = new HashMap<>();
            response.put("rol", rol);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(crearMensajeError("ID de admin inválido"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(crearMensajeError(e.getMessage()));
        }
    }

    // Métodos auxiliares para crear respuestas
    private Map<String, String> crearMensajeError(String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("error", mensaje);
        return response;
    }

    private Map<String, String> crearMensajeExito(String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return response;
    }
}