package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tipos_contrato")
@Data
public class TipoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_contrato")
    private Integer idTipoContrato;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;
}
