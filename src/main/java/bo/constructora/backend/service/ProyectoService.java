package bo.constructora.backend.service;

import bo.constructora.backend.entity.Cliente;
import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.repository.ClienteRepository;
import bo.constructora.backend.repository.ProyectoRepository;
import bo.constructora.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repo;
    private final ClienteRepository clienteRepo;
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByUsername(username)
                .map(u -> u.getIdUsuario())
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Proyecto> listarPorCliente(Integer idCliente) {
        return repo.findByCliente_IdCliente(idCliente);
    }

    @Transactional(readOnly = true)
    public Proyecto obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado: " + id));
    }

    @Transactional
    public Proyecto crear(Proyecto p) {
        if (p.getCliente() == null || p.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente válido");
        }
        Cliente cliente = clienteRepo.findById(p.getCliente().getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        p.setCliente(cliente);
        Proyecto guardado = repo.save(p);

        bitacora.registrar(getIdUsuarioActual(), "CREAR", "proyectos",
                "Proyecto creado: id=" + guardado.getIdProyecto()
                        + ", nombre=" + guardado.getNombre()
                        + ", cliente=" + cliente.getIdCliente());
        return guardado;
    }

    @Transactional
    public Proyecto actualizar(Integer id, Proyecto p) {
        Proyecto existente = obtenerPorId(id);

        if (p.getNombre() != null)              existente.setNombre(p.getNombre());
        if (p.getDescripcion() != null)         existente.setDescripcion(p.getDescripcion());
        if (p.getCiudad() != null)              existente.setCiudad(p.getCiudad());
        if (p.getDireccionObra() != null)       existente.setDireccionObra(p.getDireccionObra());
        if (p.getFechaInicio() != null)         existente.setFechaInicio(p.getFechaInicio());
        if (p.getFechaFinEstimada() != null)    existente.setFechaFinEstimada(p.getFechaFinEstimada());
        if (p.getPresupuestoAprobado() != null) existente.setPresupuestoAprobado(p.getPresupuestoAprobado());
        if (p.getPorcentajeAvance() != null)    existente.setPorcentajeAvance(p.getPorcentajeAvance());
        if (p.getEstado() != null)              existente.setEstado(p.getEstado());

        if (p.getCliente() != null && p.getCliente().getIdCliente() != null) {
            Cliente cliente = clienteRepo.findById(p.getCliente().getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            existente.setCliente(cliente);
        }

        Proyecto guardado = repo.save(existente);
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "proyectos",
                "Proyecto actualizado: id=" + id + ", nombre=" + guardado.getNombre());
        return guardado;
    }

    @Transactional
    public void eliminar(Integer id) {
        Proyecto p = obtenerPorId(id);
        repo.deleteById(id);
        bitacora.registrar(getIdUsuarioActual(), "ELIMINAR", "proyectos",
                "Proyecto eliminado: id=" + id + ", nombre=" + p.getNombre());
    }
}
