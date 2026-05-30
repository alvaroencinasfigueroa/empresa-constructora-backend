package bo.constructora.backend.service;

import bo.constructora.backend.entity.*;
import bo.constructora.backend.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepo;
    private final ContratoRepository contratoRepo;
    private final ClienteRepository clienteRepo;
    private final BitacoraService bitacora;
    private final UsuarioRepository usuarioRepo;

    private Integer getIdUsuarioActual() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return usuarioRepo.findByUsername(username).map(u -> u.getIdUsuario()).orElse(null);
        } catch (Exception e) { return null; }
    }

    @Transactional
    public FacturaResponse emitir(FacturaRequest req) {
        Contrato contrato = contratoRepo.findById(req.getIdContrato())
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado: " + req.getIdContrato()));

        Cliente cliente = contrato.getCliente();

        BigDecimal subtotal = req.getMontoSubtotal();
        BigDecimal iva = req.getPorcentajeIva() != null ? req.getPorcentajeIva() : new BigDecimal("13.00");
        BigDecimal total = subtotal.add(subtotal.multiply(iva).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        Factura f = new Factura();
        f.setContrato(contrato);
        f.setCliente(cliente);
        f.setFechaEmision(req.getFechaEmision() != null ? req.getFechaEmision() : LocalDate.now());
        f.setFechaVencimiento(req.getFechaVencimiento());
        f.setMontoSubtotal(subtotal);
        f.setPorcentajeIva(iva);
        f.setMontoTotal(total);
        f.setEstado("Emitida");

        Factura guardada = facturaRepo.save(f);
        bitacora.registrar(getIdUsuarioActual(), "CREAR", "facturas",
                "Factura emitida: id=" + guardada.getIdFactura()
                        + ", contrato=" + contrato.getIdContrato()
                        + ", total=" + total);

        return toResponse(facturaRepo.findByIdConFetch(guardada.getIdFactura()).orElseThrow());
    }

    @Transactional
    public FacturaResponse marcarPagada(Integer id) {
        Factura f = facturaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + id));
        if ("Pagada".equals(f.getEstado()))
            throw new IllegalStateException("La factura ya está pagada.");
        f.setEstado("Pagada");
        facturaRepo.save(f);
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "facturas",
                "Factura marcada como Pagada: id=" + id);
        return toResponse(facturaRepo.findByIdConFetch(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<FacturaResponse> listarTodas() {
        return facturaRepo.findAllConFetch().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacturaResponse> listarPorCliente(Integer idCliente) {
        return facturaRepo.findByClienteConFetch(idCliente).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private FacturaResponse toResponse(Factura f) {
        FacturaResponse r = new FacturaResponse();
        r.setIdFactura(f.getIdFactura());
        r.setIdContrato(f.getContrato().getIdContrato());
        r.setNombreCliente(f.getCliente().getNombre()
                + (f.getCliente().getApellido() != null ? " " + f.getCliente().getApellido() : ""));
        r.setNombreProyecto(f.getContrato().getProyecto().getNombre());
        r.setFechaEmision(f.getFechaEmision());
        r.setFechaVencimiento(f.getFechaVencimiento());
        r.setMontoSubtotal(f.getMontoSubtotal());
        r.setPorcentajeIva(f.getPorcentajeIva());
        r.setMontoTotal(f.getMontoTotal());
        r.setEstado(f.getEstado());
        return r;
    }

    // ── DTOs internos ────────────────────────────────────────────────────────
    @Data
    public static class FacturaRequest {
        private Integer idContrato;
        private BigDecimal montoSubtotal;
        private BigDecimal porcentajeIva;
        private LocalDate fechaEmision;
        private LocalDate fechaVencimiento;
    }

    @Data
    public static class FacturaResponse {
        private Integer idFactura;
        private Integer idContrato;
        private String nombreCliente;
        private String nombreProyecto;
        private LocalDate fechaEmision;
        private LocalDate fechaVencimiento;
        private BigDecimal montoSubtotal;
        private BigDecimal porcentajeIva;
        private BigDecimal montoTotal;
        private String estado;
    }
}