package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tipo_cliente")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cliente")
    private Integer idTipoCliente;

    private String nombre;
    private String descripcion;
}