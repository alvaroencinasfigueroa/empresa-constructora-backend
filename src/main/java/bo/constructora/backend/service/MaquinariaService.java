package bo.constructora.backend.service;

import bo.constructora.backend.entity.Maquinaria;
import bo.constructora.backend.repository.MaquinariaRepository;
import bo.constructora.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquinariaService {

    private final MaquinariaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByUsername(username)
                .map(u -> u.getIdUsuario())
                .orElse(null);
    }

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
        boolean esNuevo = (m.getIdMaquinaria() == null);
        Maquinaria guardada = repo.save(m);
        String accion = esNuevo ? "CREAR" : "ACTUALIZAR";
        bitacora.registrar(getIdUsuarioActual(), accion, "maquinaria",
                "Maquinaria " + accion.toLowerCase() + ": id=" + guardada.getIdMaquinaria()
                        + ", nombre=" + guardada.getNombre()
                        + ", estado=" + guardada.getEstado());
        return guardada;
    }

    @Transactional
    public Maquinaria actualizar(Integer id, Maquinaria m) {
        obtenerPorId(id);
        m.setIdMaquinaria(id);
        Maquinaria guardada = repo.save(m);
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "maquinaria",
                "Maquinaria actualizada: id=" + id
                        + ", nombre=" + guardada.getNombre()
                        + ", estado=" + guardada.getEstado());
        return guardada;
    }

    @Transactional
    public void eliminar(Integer id) {
        Maquinaria m = obtenerPorId(id);
        repo.deleteById(id);
        bitacora.registrar(getIdUsuarioActual(), "ELIMINAR", "maquinaria",
                "Maquinaria eliminada: id=" + id + ", nombre=" + m.getNombre());
    }
}
