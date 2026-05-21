package bo.constructora.backend.service;

import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repo;

    public List<Proyecto> listarTodos() {
        return repo.findAll();
    }

    public Proyecto obtenerPorId(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    public Proyecto guardar(Proyecto p) {
        return repo.save(p);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}