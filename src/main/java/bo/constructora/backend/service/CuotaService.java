package bo.constructora.backend.service;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.entity.*;
import bo.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuotaService {

    private final CuotaPagoRepository cuotaRepo;
    private final ContratoRepository contratoRepo;

    // ── Generar plan de cuotas para un contrato ──────────────────────────────
    @Transactional
    public List<CuotaResponse> generarPlan(GenerarCuotasRequest req) {
        Contrato contrato = contratoRepo.findById(req.getIdContrato())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contrato no encontrado: " + req.getIdContrato()));

        if (cuotaRepo.existsByContrato_IdContrato(req.getIdContrato())) {
            throw new IllegalStateException(
                    "Este contrato ya tiene un plan de cuotas. Elimínelo antes de regenerar.");
        }

        int n = req.getNumeroCuotas();
        BigDecimal total = contrato.getMontoTotal();
        BigDecimal montoCuota = total.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);
        BigDecimal diferencia = total.subtract(montoCuota.multiply(BigDecimal.valueOf(n)));

        List<CuotaPago> cuotas = new ArrayList<>();
        LocalDate vencimiento = req.getFechaPrimeraCuota();

        for (int i = 1; i <= n; i++) {
            CuotaPago c = new CuotaPago();
            c.setContrato(contrato);
            c.setNumeroCuota(i);
            c.setMontoEsperado(i == n ? montoCuota.add(diferencia) : montoCuota);
            c.setFechaVencimiento(vencimiento);
            c.setEstado("Pendiente");
            cuotas.add(c);
            vencimiento = vencimiento.plusDays(req.getDiasEntreCuotas());
        }

        return cuotaRepo.saveAll(cuotas).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Listar cuotas de un contrato ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<CuotaResponse> listarPorContrato(Integer idContrato) {
        return cuotaRepo.findByContrato_IdContratoOrderByNumeroCuota(idContrato)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Obtener una cuota ────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public CuotaResponse obtenerPorId(Integer id) {
        return toResponse(cuotaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada: " + id)));
    }

    // ── Actualizar monto o fecha ─────────────────────────────────────────────
    @Transactional
    public CuotaResponse actualizar(Integer id, CuotaResponse req) {
        CuotaPago c = cuotaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada: " + id));
        if (req.getMontoEsperado() != null) c.setMontoEsperado(req.getMontoEsperado());
        if (req.getFechaVencimiento() != null) c.setFechaVencimiento(req.getFechaVencimiento());
        return toResponse(cuotaRepo.save(c));
    }

    // ── Eliminar plan completo de un contrato ────────────────────────────────
    @Transactional
    public void eliminarPlan(Integer idContrato) {
        List<CuotaPago> cuotas = cuotaRepo
                .findByContrato_IdContratoOrderByNumeroCuota(idContrato);
        boolean tienePagadas = cuotas.stream()
                .anyMatch(c -> "Pagado".equals(c.getEstado()));
        if (tienePagadas) {
            throw new IllegalStateException(
                    "No se puede eliminar el plan: existen cuotas ya pagadas.");
        }
        cuotaRepo.deleteAll(cuotas);
    }

    // ── Resumen financiero ───────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ResumenFinancieroResponse resumen(Integer idContrato) {
        Contrato contrato = contratoRepo.findById(idContrato).orElseThrow();
        List<CuotaPago> cuotas = cuotaRepo
                .findByContrato_IdContratoOrderByNumeroCuota(idContrato);
        BigDecimal montoPagado = contratoRepo.sumPagosConfirmados(idContrato);

        ResumenFinancieroResponse r = new ResumenFinancieroResponse();
        r.setIdContrato(idContrato);
        r.setNombreProyecto(contrato.getProyecto().getNombre());
        r.setMontoTotal(contrato.getMontoTotal());
        r.setMontoPagado(montoPagado);
        r.setSaldoPendiente(contrato.getMontoTotal().subtract(montoPagado));
        r.setCuotasPendientes(cuotas.stream().filter(c -> "Pendiente".equals(c.getEstado())).count());
        r.setCuotasVencidas(cuotas.stream().filter(c -> "Vencido".equals(c.getEstado())).count());
        r.setCuotasPagadas(cuotas.stream().filter(c -> "Pagado".equals(c.getEstado())).count());
        return r;
    }

    // ── Job automático: marcar cuotas vencidas cada día a medianoche ─────────
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void marcarCuotasVencidas() {
        int actualizadas = cuotaRepo.marcarVencidas();
        System.out.println("[Scheduler] Cuotas marcadas como vencidas: " + actualizadas);
    }

    // ── Mapper ───────────────────────────────────────────────────────────────
    public CuotaResponse toResponse(CuotaPago c) {
        CuotaResponse r = new CuotaResponse();
        r.setIdCuota(c.getIdCuota());
        r.setNumeroCuota(c.getNumeroCuota());
        r.setMontoEsperado(c.getMontoEsperado());
        r.setFechaVencimiento(c.getFechaVencimiento());
        r.setEstado(c.getEstado());
        r.setFechaPagoReal(c.getFechaPagoReal());
        r.setIdPago(c.getPago() != null ? c.getPago().getIdPago() : null);
        return r;
    }
}