package bo.constructora.backend.service;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.entity.*;
import bo.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return usuarioRepo.findByUsername(username)
                    .map(u -> u.getIdUsuario())
                    .orElse(null);
        } catch (Exception e) {
            return null; // puede ser null en contexto de scheduler
        }
    }

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

        List<CuotaResponse> resultado = cuotaRepo.saveAll(cuotas).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        bitacora.registrar(getIdUsuarioActual(), "CREAR", "cuotas_pago",
                "Plan de cuotas generado: contrato=" + req.getIdContrato()
                        + ", cuotas=" + n + ", total=" + total);
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<CuotaResponse> listarPorContrato(Integer idContrato) {
        return cuotaRepo.findByContrato_IdContratoOrderByNumeroCuota(idContrato)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuotaResponse obtenerPorId(Integer id) {
        return toResponse(cuotaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada: " + id)));
    }

    @Transactional
    public CuotaResponse actualizar(Integer id, CuotaResponse req) {
        CuotaPago c = cuotaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuota no encontrada: " + id));
        if (req.getMontoEsperado() != null) c.setMontoEsperado(req.getMontoEsperado());
        if (req.getFechaVencimiento() != null) c.setFechaVencimiento(req.getFechaVencimiento());
        CuotaResponse resultado = toResponse(cuotaRepo.save(c));
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "cuotas_pago",
                "Cuota actualizada: id=" + id
                        + ", contrato=" + c.getContrato().getIdContrato()
                        + ", cuota#=" + c.getNumeroCuota());
        return resultado;
    }

    @Transactional
    public void eliminarPlan(Integer idContrato) {
        List<CuotaPago> cuotas = cuotaRepo.findByContrato_IdContratoOrderByNumeroCuota(idContrato);
        boolean tienePagadas = cuotas.stream().anyMatch(c -> "Pagado".equals(c.getEstado()));
        if (tienePagadas) {
            throw new IllegalStateException("No se puede eliminar el plan: existen cuotas ya pagadas.");
        }
        cuotaRepo.deleteAll(cuotas);
        bitacora.registrar(getIdUsuarioActual(), "ELIMINAR", "cuotas_pago",
                "Plan de cuotas eliminado: contrato=" + idContrato
                        + ", cuotas eliminadas=" + cuotas.size());
    }

    @Transactional(readOnly = true)
    public ResumenFinancieroResponse resumen(Integer idContrato) {
        Contrato contrato = contratoRepo.findById(idContrato).orElseThrow();
        List<CuotaPago> cuotas = cuotaRepo.findByContrato_IdContratoOrderByNumeroCuota(idContrato);
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

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void marcarCuotasVencidas() {
        int actualizadas = cuotaRepo.marcarVencidas();
        System.out.println("[Scheduler] Cuotas marcadas como vencidas: " + actualizadas);
        if (actualizadas > 0) {
            bitacora.registrar(null, "SCHEDULER", "cuotas_pago",
                    "Cuotas marcadas automáticamente como vencidas: " + actualizadas);
        }
    }

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
