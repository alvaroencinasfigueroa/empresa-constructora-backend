package bo.constructora.backend.service;

import bo.constructora.backend.entity.Material;
import bo.constructora.backend.repository.MaterialRepository;
import bo.constructora.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final BitacoraService bitacora;

    private Integer getIdUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByUsername(username)
                .map(u -> u.getIdUsuario())
                .orElse(null);
    }

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
        boolean esNuevo = (m.getIdMaterial() == null);
        Material guardado = repo.save(m);
        String accion = esNuevo ? "CREAR" : "ACTUALIZAR";
        bitacora.registrar(getIdUsuarioActual(), accion, "materiales",
                "Material " + accion.toLowerCase() + ": id=" + guardado.getIdMaterial()
                        + ", nombre=" + guardado.getNombre()
                        + ", unidad=" + guardado.getUnidad());
        return guardado;
    }

    @Transactional
    public Material actualizar(Integer id, Material m) {
        Material existente = obtenerPorId(id);
        existente.setNombre(m.getNombre());
        existente.setUnidad(m.getUnidad());
        existente.setDescripcion(m.getDescripcion());
        existente.setPrecioRef(m.getPrecioRef());
        Material guardado = repo.save(existente);
        bitacora.registrar(getIdUsuarioActual(), "ACTUALIZAR", "materiales",
                "Material actualizado: id=" + id + ", nombre=" + guardado.getNombre());
        return guardado;
    }

    @Transactional
    public void eliminar(Integer id) {
        Material m = obtenerPorId(id);
        repo.deleteById(id);
        bitacora.registrar(getIdUsuarioActual(), "ELIMINAR", "materiales",
                "Material eliminado: id=" + id + ", nombre=" + m.getNombre());
    }
}
