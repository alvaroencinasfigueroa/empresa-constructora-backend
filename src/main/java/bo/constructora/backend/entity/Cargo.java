package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cargos")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer idCargo;

    private String nombre;

    @Column(name = "nivel_salarial")
    private String nivelSalarial;

    private String descripcion;
}