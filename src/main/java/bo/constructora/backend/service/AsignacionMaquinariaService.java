package bo.constructora.backend.service;

import bo.constructora.backend.entity.AsignacionMaquinaria;
import bo.constructora.backend.repository.AsignacionMaquinariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionMaquinariaService {

    private final AsignacionMaquinariaRepository repo;

    @Transactional(readOnly = true)
    public List<AsignacionMaquinaria> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<AsignacionMaquinaria> listarPorProyecto(Integer idProyecto) {
        return repo.findByProyecto_IdProyecto(idProyecto);
    }

    @Transactional(readOnly = true)
    public List<AsignacionMaquinaria> listarPorMaquinaria(Integer idMaquinaria) {
        return repo.findByMaquinaria_IdMaquinaria(idMaquinaria);
    }

    @Transactional(readOnly = true)
    public List<AsignacionMaquinaria> listarPorEmpleado(Integer idEmpleado) {
        return repo.findByEmpleadoResponsable_IdEmpleado(idEmpleado);
    }

    @Transactional(readOnly = true)
    public AsignacionMaquinaria obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación de maquinaria no encontrada: " + id));
    }

    @Transactional
    public AsignacionMaquinaria guardar(AsignacionMaquinaria a) {
        return repo.save(a);
    }

    @Transactional
    public AsignacionMaquinaria actualizar(Integer id, AsignacionMaquinaria a) {
        obtenerPorId(id);
        a.setIdAsigMaquinaria(id);
        return repo.save(a);
    }

    @Transactional
    public void eliminar(Integer id) {
        obtenerPorId(id);
        repo.deleteById(id);
    }
}
