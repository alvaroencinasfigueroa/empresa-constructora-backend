package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "proyectos")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Integer idProyecto;

    @Column(name = "id_cliente", insertable = false, updatable = false)
    private Integer idCliente;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "direccion_obra")
    private String direccionObra;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "presupuesto_aprobado")
    private BigDecimal presupuestoAprobado;

    @Column(name = "porcentaje_avance")
    private BigDecimal porcentajeAvance;

    @Column(name = "estado")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cliente cliente;
}