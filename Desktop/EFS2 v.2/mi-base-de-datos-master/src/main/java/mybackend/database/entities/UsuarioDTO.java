package mybackend.database.entities;

import java.time.Instant;

public class UsuarioDTO {
    
    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private String estado;
    private Instant fechaCreacion;

    // Constructor por defecto
    public UsuarioDTO() {}

    // Constructor desde entidad Usuario
    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getEmail();
        this.rol = usuario.getRol();
        this.estado = usuario.getEstado();
        this.fechaCreacion = usuario.getFechaCreacion();
    }

    // Constructor con parámetros
    public UsuarioDTO(Long id, String nombre, String email, String rol, String estado, Instant fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Métodos de utilidad
    public boolean isActivo() {
        return "activo".equals(this.estado);
    }

    public boolean isSuperAdmin() {
        return "superadmin".equals(this.rol);
    }

    public boolean isVendedor() {
        return "vendedor".equals(this.rol);
    }

    public boolean isCliente() {
        return "cliente".equals(this.rol);
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}