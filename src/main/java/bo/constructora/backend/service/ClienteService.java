package bo.constructora.backend.service;

import bo.constructora.backend.dto.ClienteRegistroRequest;
import bo.constructora.backend.entity.Cliente;
import bo.constructora.backend.entity.Usuario;
import bo.constructora.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepo;          // antes lo llamabas "repo"
    private final TipoClienteRepository tipoClienteRepo;
    private final UsuarioRepository usuarioRepo;
    private final RolUsuarioRepository rolUsuarioRepo;

    // Métodos básicos (ajustados al nombre del repositorio)
    public List<Cliente> listarTodos() {
        return clienteRepo.findAll();
    }

    public Cliente obtenerPorId(Integer id) {
        return clienteRepo.findById(id).orElseThrow();
    }

    public Cliente guardar(Cliente c) {
        return clienteRepo.save(c);
    }

    public void eliminar(Integer id) {
        clienteRepo.deleteById(id);
    }

    @Transactional
    public Cliente registrarCliente(ClienteRegistroRequest req, PasswordEncoder encoder) {
        // Validar unicidad de RUC_NIT
        if (clienteRepo.findByRucNit(req.getRucNit()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con ese RUC/NIT");
        }

        Cliente c = new Cliente();
        c.setNombre(req.getNombre());
        c.setApellido(req.getApellido());
        c.setRucNit(req.getRucNit());
        c.setDireccion(req.getDireccion());
        c.setTelefono(req.getTelefono());
        c.setEmail(req.getEmail());
        c.setFechaRegistro(LocalDate.now());
        c.setEstado(true);
        // Asignar tipo de cliente por defecto (Persona Natural)
        c.setTipoCliente(tipoClienteRepo.findById(1)
                .orElseThrow(() -> new IllegalStateException("Tipo de cliente por defecto no encontrado")));
        c = clienteRepo.save(c);

        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setCliente(c);
        u.setRol(rolUsuarioRepo.findByNombre("CLIENTE")
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no encontrado")));
        u.setEstado(true);
        usuarioRepo.save(u);

        return c;
    }
}