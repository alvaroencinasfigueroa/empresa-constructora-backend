package bo.constructora.backend.service;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.entity.*;
import bo.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepo;
    private final ContratoRepository contratoRepo;
    private final CuotaPagoRepository cuotaRepo;
    private final MetodoPagoRepository metodoPagoRepo;

    // ── Registrar un pago ────────────────────────────────────────────────────
    @Transactional
    public PagoResponse registrar(PagoRequest req) {
        Contrato contrato = contratoRepo.findById(req.getIdContrato())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contrato no encontrado: " + req.getIdContrato()));

        if (!"Vigente".equals(contrato.getEstado())) {
            throw new IllegalStateException("El contrato no está vigente.");
        }

        MetodoPago metodo = metodoPagoRepo.findById(req.getIdMetodoPago())
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no encontrado."));

        // Guardar el pago
        Pago pago = new Pago();
        pago.setContrato(contrato);
        pago.setFechaPago(req.getFechaPago() != null ? req.getFechaPago() : LocalDate.now());
        pago.setMonto(req.getMonto());
        pago.setMetodoPago(metodo);
        pago.setNumeroComprobante(req.getNumeroComprobante());
        pago.setEstado("Confirmado");
        pago = pagoRepo.save(pago);

        // Si se indicó una cuota, marcarla como Pagada
        if (req.getIdCuota() != null) {
            CuotaPago cuota = cuotaRepo.findById(req.getIdCuota())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Cuota no encontrada: " + req.getIdCuota()));
            if (!"Pagado".equals(cuota.getEstado())) {
                cuota.setEstado("Pagado");
                cuota.setPago(pago);
                cuota.setFechaPagoReal(pago.getFechaPago());
                cuotaRepo.save(cuota);
            }
        }

        return toResponse(pago);
    }

    // ── Anular un pago (y revertir la cuota si aplica) ───────────────────────
    @Transactional
    public PagoResponse anular(Integer idPago) {
        Pago pago = pagoRepo.findById(idPago)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + idPago));

        if ("Anulado".equals(pago.getEstado())) {
            throw new IllegalStateException("El pago ya está anulado.");
        }

        pago.setEstado("Anulado");
        pagoRepo.save(pago);

        // Revertir la cuota vinculada si existe
        cuotaRepo.findAll().stream()
                .filter(c -> c.getPago() != null && c.getPago().getIdPago().equals(idPago))
                .forEach(c -> {
                    c.setEstado("Pendiente");
                    c.setPago(null);
                    c.setFechaPagoReal(null);
                    cuotaRepo.save(c);
                });

        return toResponse(pago);
    }

    // ── Listar pagos de un contrato ──────────────────────────────────────────
    public List<PagoResponse> listarPorContrato(Integer idContrato) {
        return pagoRepo.findByContrato_IdContratoOrderByFechaPagoDesc(idContrato)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Obtener un pago ──────────────────────────────────────────────────────
    public PagoResponse obtenerPorId(Integer id) {
        return toResponse(pagoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + id)));
    }

    // ── Mapper ───────────────────────────────────────────────────────────────
    private PagoResponse toResponse(Pago p) {
        PagoResponse r = new PagoResponse();
        r.setIdPago(p.getIdPago());
        r.setIdContrato(p.getContrato().getIdContrato());
        r.setNombreCliente(p.getContrato().getCliente().getNombre()
                + (p.getContrato().getCliente().getApellido() != null
                ? " " + p.getContrato().getCliente().getApellido() : ""));
        r.setNombreProyecto(p.getContrato().getProyecto().getNombre());
        r.setFechaPago(p.getFechaPago());
        r.setMonto(p.getMonto());
        r.setMetodoPago(p.getMetodoPago().getNombre());
        r.setNumeroComprobante(p.getNumeroComprobante());
        r.setEstado(p.getEstado());
        return r;
    }
}
