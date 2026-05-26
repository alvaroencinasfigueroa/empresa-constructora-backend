/*package bo.constructora.backend.service;

import bo.constructora.backend.entity.Cliente;
import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.repository.ClienteRepository;
import bo.constructora.backend.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repo;
    private final ClienteRepository clienteRepo;

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
        return repo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Proyecto no encontrado: " + id));
    }

    @Transactional
    public Proyecto guardar(Proyecto p) {
        // Si viene idCliente como referencia, cargar el cliente
        if (p.getCliente() != null && p.getCliente().getIdCliente() != null) {
            Cliente cliente = clienteRepo.findById(p.getCliente().getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            p.setCliente(cliente);
        }
        return repo.save(p);
    }

    @Transactional
    public void eliminar(Integer id) {
        obtenerPorId(id);
        repo.deleteById(id);
    }
}*/

package bo.constructora.backend.service;

import bo.constructora.backend.entity.Cliente;
import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.repository.ClienteRepository;
import bo.constructora.backend.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repo;
    private final ClienteRepository clienteRepo;

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
        // Validar que el cliente sea obligatorio al crear
        if (p.getCliente() == null || p.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente válido");
        }
        // Cargar la entidad completa del cliente desde BD
        Cliente cliente = clienteRepo.findById(p.getCliente().getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        p.setCliente(cliente);
        return repo.save(p);
    }

    @Transactional
    public Proyecto actualizar(Integer id, Proyecto p) {
        Proyecto existente = obtenerPorId(id); // Valida que exista

        // Actualizar campos simples (solo si vienen con valor)
        if (p.getNombre() != null) existente.setNombre(p.getNombre());
        if (p.getDescripcion() != null) existente.setDescripcion(p.getDescripcion());
        if (p.getCiudad() != null) existente.setCiudad(p.getCiudad());
        if (p.getDireccionObra() != null) existente.setDireccionObra(p.getDireccionObra());
        if (p.getFechaInicio() != null) existente.setFechaInicio(p.getFechaInicio());
        if (p.getFechaFinEstimada() != null) existente.setFechaFinEstimada(p.getFechaFinEstimada());
        if (p.getPresupuestoAprobado() != null) existente.setPresupuestoAprobado(p.getPresupuestoAprobado());
        if (p.getPorcentajeAvance() != null) existente.setPorcentajeAvance(p.getPorcentajeAvance());
        if (p.getEstado() != null) existente.setEstado(p.getEstado());

        // ⚠️ Solo actualizar el cliente si se envía explícitamente uno nuevo
        if (p.getCliente() != null && p.getCliente().getIdCliente() != null) {
            Cliente cliente = clienteRepo.findById(p.getCliente().getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            existente.setCliente(cliente);
        }
        // Si p.getCliente() es null → se mantiene el cliente original (NO se modifica)

        return repo.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        obtenerPorId(id); // Valida que exista antes de eliminar
        repo.deleteById(id);
    }
}