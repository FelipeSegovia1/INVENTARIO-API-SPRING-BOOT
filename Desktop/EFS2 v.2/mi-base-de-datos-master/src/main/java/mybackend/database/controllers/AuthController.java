package mybackend.database.controllers;

import mybackend.database.entities.LoginRequest;
import mybackend.database.entities.UsuarioDTO;
import mybackend.database.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174","http://localhost:5175","http://localhost:5176","http://localhost:5177"})
@Tag(name = "Autenticación", description = "Endpoints de login y utilidades de autenticación")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint de login
     * POST /api/login
     * @param loginRequest credenciales del usuario
     * @return UsuarioDTO si las credenciales son válidas
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y retorna información del usuario")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            UsuarioDTO usuario = usuarioService.validarCredenciales(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );

            // Crear respuesta exitosa con información del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("usuario", usuario);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Crear respuesta de error
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Endpoint para verificar si un usuario existe por email
     * GET /api/check-email/{email}
     * @param email el email a verificar
     * @return información sobre la existencia del email
     */
    @GetMapping("/check-email/{email}")
    @Operation(summary = "Verificar existencia de email", description = "Consulta si el email está registrado")
    public ResponseEntity<?> checkEmail(@Parameter(description = "Email a verificar") @PathVariable String email) {
        try {
            UsuarioDTO usuario = usuarioService.buscarPorEmail(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("exists", true);
            response.put("usuario", usuario);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("exists", false);
            response.put("message", "Email no encontrado");

            return ResponseEntity.ok(response);
        }
    }

    /**
     * Endpoint para logout (simple)
     * POST /api/logout
     * @return mensaje de logout exitoso
     */
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Finaliza la sesión actual (sin token)")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logout exitoso");

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para validar si un usuario es superadmin
     * GET /api/validate-admin/{id}
     * @param id el ID del usuario a validar
     * @return información sobre si es superadmin
     */
    @GetMapping("/validate-admin/{id}")
    @Operation(summary = "Validar superadministrador", description = "Verifica si el usuario indicado es superadmin")
    public ResponseEntity<?> validateAdmin(@Parameter(description = "ID del usuario a validar") @PathVariable Long id) {
        try {
            boolean isSuperAdmin = usuarioService.esSuperAdmin(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("isSuperAdmin", isSuperAdmin);
            response.put("userId", id);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("isSuperAdmin", false);
            response.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}