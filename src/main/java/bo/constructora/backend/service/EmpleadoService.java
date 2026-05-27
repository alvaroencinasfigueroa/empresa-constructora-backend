package bo.constructora.backend.service;

import bo.constructora.backend.entity.Empleado;
import bo.constructora.backend.repository.CargoRepository;
import bo.constructora.backend.repository.CategoriaEmpleadoRepository;
import bo.constructora.backend.repository.DepartamentoRepository;
import bo.constructora.backend.repository.EmpleadoRepository;
import bo.constructora.backend.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final CargoRepository cargoRepo;
    private final EspecialidadRepository especialidadRepo;
    private final DepartamentoRepository departamentoRepo;
    private final CategoriaEmpleadoRepository categoriaRepo;

    @Transactional(readOnly = true)
    public List<Empleado> listarTodos() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado: " + id));
    }

    @Transactional
    public Empleado guardar(Empleado e) {
        if ((e.getCargo() == null || e.getCargo().getIdCargo() == null) && e.getIdEmpleado() != null) {
            Empleado existente = obtenerPorId(e.getIdEmpleado());
            e.setCargo(existente.getCargo());
        } else if (e.getCargo() != null && e.getCargo().getIdCargo() != null) {
            e.setCargo(cargoRepo.findById(e.getCargo().getIdCargo())
                    .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado")));
        }
        if (e.getEspecialidad() != null && e.getEspecialidad().getIdEspecialidad() != null) {
            e.setEspecialidad(especialidadRepo.findById(e.getEspecialidad().getIdEspecialidad())
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada")));
        }
        if (e.getDepartamento() != null && e.getDepartamento().getIdDepartamento() != null) {
            e.setDepartamento(departamentoRepo.findById(e.getDepartamento().getIdDepartamento())
                    .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado")));
        }
        if (e.getCategoria() != null && e.getCategoria().getIdCategoria() != null) {
            e.setCategoria(categoriaRepo.findById(e.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada")));
        }
        return repo.save(e);
    }

    @Transactional
    public void eliminar(Integer id) {
        obtenerPorId(id);
        repo.deleteById(id);
    }
}