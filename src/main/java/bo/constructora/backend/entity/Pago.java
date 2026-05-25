package bo.constructora.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_factura")
    private Integer idFactura;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "numero_comprobante")
    private String numeroComprobante;

    @Column(name = "estado", nullable = false)
    private String estado = "Confirmado";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    @JsonIgnore
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    @JsonIgnore
    private MetodoPago metodoPago;
}