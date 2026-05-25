package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuotas_pago")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CuotaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuota")
    private Integer idCuota;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;

    @Column(name = "monto_esperado", nullable = false)
    private BigDecimal montoEsperado;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "estado", nullable = false)
    private String estado = "Pendiente";

    @Column(name = "fecha_pago_real")
    private LocalDate fechaPagoReal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    @JsonIgnore
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pago")
    @JsonIgnore
    private Pago pago;
}