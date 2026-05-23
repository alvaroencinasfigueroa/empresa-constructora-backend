package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "metodo_pago")
@Data
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
