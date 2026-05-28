package bo.constructora.backend.service;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.entity.*;
import bo.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepo;
    private final ClienteRepository clienteRepo;
    private final ProyectoRepository proyectoRepo;
    private final TipoContratoRepository tipoContratoRepo;
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByUsername(username)
                .map(u -> u.getIdUsuario())
                .orElse(null);
    }

    @Transactional
    public ContratoResponse crear(ContratoRequest req) {
        Contrato c = new Contrato();
        c.setCliente(clienteRepo.findById(req.getIdCliente()).orElseThrow(
                () -> new IllegalArgumentException("Cliente no encontrado: " + req.getIdCliente())));
        c.setProyecto(proyectoRepo.findById(req.getIdProyecto()).orElseThrow(
                () -> new IllegalArgumentException("Proyecto no encontrado: " + req.getIdProyecto())));
        c.setTipoContrato(tipoContratoRepo.findById(req.getIdTipoContrato()).orElseThrow(
                () -> new IllegalArgumentException("Tipo contrato no encontrado")));
        c.setFechaFirma(req.getFechaFirma());
        c.setMontoTotal(req.getMontoTotal());
        c.setFechaInicio(req.getFechaInicio());
        c.setFechaFin(req.getFechaFin());
        c.setEstado("Vigente");

        Proyecto proyecto = c.getProyecto();
        if (proyecto.getCliente() == null) {
            proyecto.setCliente(c.getCliente());
            proyectoRepo.save(proyecto);
        }

        ContratoResponse response = toResponse(contratoRepo.save(c), BigDecimal.ZERO);
        bitacora.registrar(getIdUsuarioActual(), "CREAR", "contratos",
                "Contrato creado: id=" + response.getIdContrato()
                        + ", cliente=" + req.getIdCliente()
                        + ", proyecto=" + req.getIdProyecto()
                        + ", monto=" + req.getMontoTotal());
        return response;
    }

    @Transactional(readOnly = true)
    public List<ContratoResponse> listarTodos() {
        return contratoRepo.findAllConFetch().stream()
                .map(c -> toResponse(c, contratoRepo.sumPagosConfirmados(c.getIdContrato())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContratoResponse> listarPorCliente(Integer idCliente) {
        return contratoRepo.findByClienteConFetch(idCliente).stream()
                .map(c -> toResponse(c, contratoRepo.sumPagosConfirmados(c.getIdContrato())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContratoResponse obtenerPorId(Integer id) {
        Contrato c = contratoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado: " + id));
        return toResponse(c, contratoRepo.sumPagosConfirmados(id));
    }

    @Transactional
    public ContratoResponse cambiarEstado(Integer id, String nuevoEstado) {
        Contrato c = contratoRepo.findById(id).orElseThrow();
        String estadoAnterior = c.getEstado();
        c.setEstado(nuevoEstado);
        ContratoResponse response = toResponse(contratoRepo.save(c), contratoRepo.sumPagosConfirmados(id));
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "contratos",
                "Estado de contrato cambiado: id=" + id
                        + ", de=" + estadoAnterior + ", a=" + nuevoEstado);
        return response;
    }

    public ContratoResponse toResponse(Contrato c, BigDecimal montoPagado) {
        ContratoResponse r = new ContratoResponse();
        r.setIdContrato(c.getIdContrato());
        r.setNombreCliente(c.getCliente().getNombre()
                + (c.getCliente().getApellido() != null ? " " + c.getCliente().getApellido() : ""));
        r.setNombreProyecto(c.getProyecto().getNombre());
        r.setTipoContrato(c.getTipoContrato().getNombre());
        r.setFechaFirma(c.getFechaFirma());
        r.setMontoTotal(c.getMontoTotal());
        r.setMontoPagado(montoPagado);
        r.setSaldoPendiente(c.getMontoTotal().subtract(montoPagado));
        r.setFechaInicio(c.getFechaInicio());
        r.setFechaFin(c.getFechaFin());
        r.setEstado(c.getEstado());
        return r;
    }
}
