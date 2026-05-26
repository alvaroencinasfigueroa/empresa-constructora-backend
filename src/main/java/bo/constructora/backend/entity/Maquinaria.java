package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "maquinaria")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Maquinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maquinaria")
    private Integer idMaquinaria;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "modelo", length = 80)
    private String modelo;

    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @Column(name = "estado", length = 20)
    private String estado;
}