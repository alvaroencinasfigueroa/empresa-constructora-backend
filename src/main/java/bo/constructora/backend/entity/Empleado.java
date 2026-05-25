package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "CI")
    private String ci;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "salario_base")
    private BigDecimal salarioBase;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "estado")
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo", nullable = false)
    @JsonIgnore
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_especialidad")
    @JsonIgnore
    private Especialidad especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento")
    @JsonIgnore
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    @JsonIgnore
    private CategoriaEmpleado categoria;
}