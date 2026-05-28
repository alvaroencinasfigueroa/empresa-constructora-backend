package bo.constructora.backend.service;

import bo.constructora.backend.entity.AsignacionMaquinaria;
import bo.constructora.backend.repository.AsignacionMaquinariaRepository;
import bo.constructora.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionMaquinariaService {

    private final AsignacionMaquinariaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByUsername(username)
                .map(u -> u.getIdUsuario())
                .orElse(null);
    }

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
        boolean esNuevo = (a.getIdAsigMaquinaria() == null);
        AsignacionMaquinaria guardada = repo.save(a);
        String accion = esNuevo ? "CREAR" : "ACTUALIZAR";
        bitacora.registrar(getIdUsuarioActual(), accion, "asignacion_maquinaria",
                "Asignación de maquinaria " + accion.toLowerCase()
                        + ": id=" + guardada.getIdAsigMaquinaria()
                        + ", maquinaria=" + (guardada.getMaquinaria() != null ? guardada.getMaquinaria().getIdMaquinaria() : "?")
                        + ", proyecto=" + (guardada.getProyecto() != null ? guardada.getProyecto().getIdProyecto() : "?"));
        return guardada;
    }

    @Transactional
    public AsignacionMaquinaria actualizar(Integer id, AsignacionMaquinaria a) {
        obtenerPorId(id);
        a.setIdAsigMaquinaria(id);
        AsignacionMaquinaria guardada = repo.save(a);
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "asignacion_maquinaria",
                "Asignación actualizada: id=" + id);
        return guardada;
    }

    @Transactional
    public void eliminar(Integer id) {
        AsignacionMaquinaria a = obtenerPorId(id);
        repo.deleteById(id);
        bitacora.registrar(getIdUsuarioActual(), "ELIMINAR", "asignacion_maquinaria",
                "Asignación eliminada: id=" + id
                        + ", maquinaria=" + (a.getMaquinaria() != null ? a.getMaquinaria().getIdMaquinaria() : "?")
                        + ", proyecto=" + (a.getProyecto() != null ? a.getProyecto().getIdProyecto() : "?"));
    }
}
