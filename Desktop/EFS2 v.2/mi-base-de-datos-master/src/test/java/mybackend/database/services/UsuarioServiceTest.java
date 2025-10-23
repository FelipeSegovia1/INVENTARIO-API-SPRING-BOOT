package mybackend.database.services;

import mybackend.database.entities.Usuario;
import mybackend.database.entities.UsuarioDTO;
import mybackend.database.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Usuario usuarioGuardado;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setRol("cliente");
        usuario.setEstado("activo");

        usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre("Test User");
        usuarioGuardado.setEmail("test@example.com");
        usuarioGuardado.setPassword("$2a$10$encodedPassword");
        usuarioGuardado.setRol("cliente");
        usuarioGuardado.setEstado("activo");
        usuarioGuardado.setFechaCreacion(Instant.now());
    }

    @Test
    void testCrearUsuario_Exitoso() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Act
        UsuarioDTO resultado = usuarioService.crearUsuario(usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("Test User", resultado.getNombre());
        assertEquals("test@example.com", resultado.getEmail());
        assertEquals("cliente", resultado.getRol());
        assertEquals("activo", resultado.getEstado());

        verify(usuarioRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testCrearUsuario_EmailDuplicado() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.crearUsuario(usuario));
        
        assertEquals("Ya existe un usuario con el email: test@example.com", exception.getMessage());
        
        verify(usuarioRepository).existsByEmail("test@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testValidarCredenciales_Exitoso() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioGuardado));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act
        UsuarioDTO resultado = usuarioService.validarCredenciales("test@example.com", "password123");

        // Assert
        assertNotNull(resultado);
        assertEquals("Test User", resultado.getNombre());
        assertEquals("test@example.com", resultado.getEmail());
        assertEquals("cliente", resultado.getRol());
        // ... existing code ...
        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
    }

    @Test
    void testValidarCredenciales_UsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.validarCredenciales("test@example.com", "password123"));
        
        assertEquals("Credenciales inválidas", exception.getMessage());
        
        verify(usuarioRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testValidarCredenciales_PasswordIncorrecto() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioGuardado));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.validarCredenciales("test@example.com", "wrongpassword"));
        
        assertEquals("Credenciales inválidas", exception.getMessage());
        
        verify(usuarioRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongpassword", "$2a$10$encodedPassword");
    }

    @Test
    void testValidarCredenciales_UsuarioInactivo() {
        // Arrange
        usuarioGuardado.setEstado("inactivo");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioGuardado));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> usuarioService.validarCredenciales("test@example.com", "password123"));
        
        assertEquals("Usuario inactivo", exception.getMessage());
        
        verify(usuarioRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}