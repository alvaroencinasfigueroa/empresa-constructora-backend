package bo.constructora.backend.service;

import bo.constructora.backend.entity.Material;
import bo.constructora.backend.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository repo;

    public List<Material> listarTodos() {
        return repo.findAll();
    }

    public List<Material> buscar(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    public Material obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado: " + id));
    }

    @Transactional
    public Material guardar(Material m) {
        return repo.save(m);
    }

    @Transactional
    public Material actualizar(Integer id, Material m) {
        Material existente = obtenerPorId(id);
        existente.setNombre(m.getNombre());
        existente.setUnidad(m.getUnidad());
        existente.setDescripcion(m.getDescripcion());
        existente.setPrecioRef(m.getPrecioRef());
        return repo.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        obtenerPorId(id); // valida que existe
        repo.deleteById(id);
    }
}