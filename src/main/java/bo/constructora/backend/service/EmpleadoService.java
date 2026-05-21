package bo.constructora.backend.service;

import bo.constructora.backend.entity.Empleado;
import bo.constructora.backend.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;

    public List<Empleado> listarTodos() { return repo.findAll(); }
    public Empleado obtenerPorId(Integer id) { return repo.findById(id).orElseThrow(); }
    public Empleado guardar(Empleado e) { return repo.save(e); }
    public void eliminar(Integer id) { repo.deleteById(id); }
}