package bo.constructora.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PagosDTO {

    @Data
    public static class ContratoRequest {
        private Integer idCliente;
        private Integer idProyecto;
        private Integer idTipoContrato;
        private LocalDate fechaFirma;
        private BigDecimal montoTotal;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
    }

    // ── Response: resumen de contrato ────────────────────────────────────────
    @Data
    public static class ContratoResponse {
        private Integer idContrato;
        private String nombreCliente;
        private String nombreProyecto;
        private String tipoContrato;
        private LocalDate fechaFirma;
        private BigDecimal montoTotal;
        private BigDecimal montoPagado;
        private BigDecimal saldoPendiente;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private String estado;
        private List<CuotaResponse> cuotas;
    }

    // ── Request: generar plan de cuotas ─────────────────────────────────────
    @Data
    public static class GenerarCuotasRequest {
        private Integer idContrato;
        private Integer numeroCuotas;          // cuántas cuotas dividir el total
        private LocalDate fechaPrimeraCuota;   // vencimiento de la cuota 1
        private Integer diasEntreCuotas;       // ej: 30 para mensual
    }

    // ── Request: registrar pago ──────────────────────────────────────────────
    @Data
    public static class PagoRequest {
        private Integer idContrato;
        private LocalDate fechaPago;
        private BigDecimal monto;
        private Integer idMetodoPago;
        private String numeroComprobante;
        private Integer idCuota;  // opcional: qué cuota cancela
    }

    // ── Response: pago ───────────────────────────────────────────────────────
    @Data
    public static class PagoResponse {
        private Integer idPago;
        private Integer idContrato;
        private String nombreCliente;
        private String nombreProyecto;
        private LocalDate fechaPago;
        private BigDecimal monto;
        private String metodoPago;
        private String numeroComprobante;
        private String estado;
    }

    // ── Response: cuota ──────────────────────────────────────────────────────
    @Data
    public static class CuotaResponse {
        private Integer idCuota;
        private Integer numeroCuota;
        private BigDecimal montoEsperado;
        private LocalDate fechaVencimiento;
        private String estado;
        private LocalDate fechaPagoReal;
        private Integer idPago;
    }

    // ── Response: resumen financiero de un contrato ──────────────────────────
    @Data
    public static class ResumenFinancieroResponse {
        private Integer idContrato;
        private String nombreProyecto;
        private BigDecimal montoTotal;
        private BigDecimal montoPagado;
        private BigDecimal saldoPendiente;
        private long cuotasPendientes;
        private long cuotasVencidas;
        private long cuotasPagadas;
    }
}
