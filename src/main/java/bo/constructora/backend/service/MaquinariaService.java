package bo.constructora.backend.service;

import bo.constructora.backend.entity.Maquinaria;
import bo.constructora.backend.repository.MaquinariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquinariaService {

    private final MaquinariaRepository repo;

    @Transactional(readOnly = true)
    public List<Maquinaria> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Maquinaria> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public List<Maquinaria> listarPorEstado(String estado) {
        return repo.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Maquinaria obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquinaria no encontrada: " + id));
    }

    @Transactional
    public Maquinaria guardar(Maquinaria m) {
        return repo.save(m);
    }

    @Transactional
    public Maquinaria actualizar(Integer id, Maquinaria m) {
        obtenerPorId(id);
        m.setIdMaquinaria(id);
        return repo.save(m);
    }

    @Transactional
    public void eliminar(Integer id) {
        obtenerPorId(id);
        repo.deleteById(id);
    }
}