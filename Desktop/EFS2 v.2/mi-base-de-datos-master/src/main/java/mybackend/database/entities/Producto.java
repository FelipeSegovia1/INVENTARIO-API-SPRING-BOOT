package mybackend.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(nullable = false)
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Instant fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;

    // Constructor adicional sin ID para creación
    public Producto(String nombre, String descripcion, BigDecimal precio, Integer stock, String imagenUrl, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
        this.activo = true;
    }

    // Métodos de utilidad
    public boolean isLowStock() {
        return this.stock != null && this.stock < 5;
    }

    public boolean isOutOfStock() {
        return this.stock != null && this.stock == 0;
    }

    public void decrementStock(int cantidad) {
        if (this.stock != null && this.stock >= cantidad) {
            this.stock -= cantidad;
        } else {
            throw new RuntimeException("Stock insuficiente");
        }
    }

    public void incrementStock(int cantidad) {
        if (this.stock == null) {
            this.stock = cantidad;
        } else {
            this.stock += cantidad;
        }
    }
}
