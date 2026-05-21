package bo.constructora.backend.service;

import bo.constructora.backend.entity.Cliente;
import bo.constructora.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repo;

    public List<Cliente> listarTodos() { return repo.findAll(); }
    public Cliente obtenerPorId(Integer id) { return repo.findById(id).orElseThrow(); }
    public Cliente guardar(Cliente c) { return repo.save(c); }
    public void eliminar(Integer id) { repo.deleteById(id); }
}