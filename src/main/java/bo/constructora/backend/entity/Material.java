package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "materiales")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Integer idMaterial;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "unidad")
    private String unidad;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_ref")
    private BigDecimal precioRef;
}