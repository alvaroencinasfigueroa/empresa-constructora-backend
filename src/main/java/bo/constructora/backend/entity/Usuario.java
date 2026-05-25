package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "estado")
    private Boolean estado = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    @JsonIgnore
    private RolUsuario rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado")
    @JsonIgnore
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    @JsonIgnore
    private Cliente cliente;
}