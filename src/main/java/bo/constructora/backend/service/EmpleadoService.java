package bo.constructora.backend.service;

import bo.constructora.backend.entity.Empleado;
import bo.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByUsername(username)
                .map(u -> u.getIdUsuario())
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado: " + id));
    }

    @Transactional
    public Empleado guardar(Empleado e) {
        boolean esNuevo = (e.getIdEmpleado() == null);

        // Resolver Cargo (obligatorio)
        Integer cargoId = (e.getCargo() != null) ? e.getCargo().getIdCargo() : e.getIdCargo();
        if (cargoId == null) throw new IllegalArgumentException("Cargo requerido");
        e.setCargo(cargoRepo.findById(cargoId)
                .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado")));

        // Resolver opcionales igual — usando el campo plano como fallback
        Integer espId = (e.getEspecialidad() != null) ? e.getEspecialidad().getIdEspecialidad() : e.getIdEspecialidad();
        if (espId != null) {
            e.setEspecialidad(especialidadRepo.findById(espId)
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada")));
        }

        Integer deptId = (e.getDepartamento() != null) ? e.getDepartamento().getIdDepartamento() : e.getIdDepartamento();
        if (deptId != null) {
            e.setDepartamento(departamentoRepo.findById(deptId)
                    .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado")));
        }

        Integer catId = (e.getCategoria() != null) ? e.getCategoria().getIdCategoria() : e.getIdCategoria();
        if (catId != null) {
            e.setCategoria(categoriaRepo.findById(catId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada")));
        }

        Empleado guardado = repo.save(e);
        String accion = esNuevo ? "CREAR" : "ACTUALIZAR";
        bitacora.registrar(getIdUsuarioActual(), accion, "empleados",
                "Empleado " + accion.toLowerCase() + ": id=" + guardado.getIdEmpleado()
                        + ", nombre=" + guardado.getNombre() + " " + guardado.getApellido());
        return guardado;
    }

    @Transactional
    public void eliminar(Integer id) {
        Empleado e = obtenerPorId(id);
        repo.deleteById(id);
        bitacora.registrar(getIdUsuarioActual(), "ELIMINAR", "empleados",
                "Empleado eliminado: id=" + id
                        + ", nombre=" + e.getNombre() + " " + e.getApellido());
    }
}
