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
}