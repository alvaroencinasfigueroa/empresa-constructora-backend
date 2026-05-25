package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "categorias_empleado")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CategoriaEmpleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    private String nombre;
    private String descripcion;
}