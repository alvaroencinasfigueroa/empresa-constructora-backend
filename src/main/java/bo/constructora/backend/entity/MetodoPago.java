package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "metodo_pago")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}