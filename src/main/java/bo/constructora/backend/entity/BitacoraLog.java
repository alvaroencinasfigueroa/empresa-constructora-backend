package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora_log")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BitacoraLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "accion", nullable = false)
    private String accion;

    @Column(name = "tabla_afectada")
    private String tablaAfectada;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;
}