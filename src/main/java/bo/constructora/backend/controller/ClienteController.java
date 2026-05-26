package bo.constructora.backend.controller;

import bo.constructora.backend.dto.ClienteRegistroRequest;
import bo.constructora.backend.entity.Cliente;
import bo.constructora.backend.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final PasswordEncoder passwordEncoder;

    // Cualquier autenticado puede listar
    @GetMapping
    public List<Cliente> listar() { return service.listarTodos(); }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Solo el ADMINISTRADOR puede crear, actualizar o eliminar clientes
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Cliente> crear(@RequestBody ClienteRegistroRequest req) {
        Cliente c = service.registrarCliente(req, passwordEncoder);
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id, @RequestBody Cliente c) {
        Cliente existente = service.obtenerPorId(id);
        existente.setNombre(c.getNombre());
        existente.setApellido(c.getApellido());
        existente.setRucNit(c.getRucNit());
        existente.setDireccion(c.getDireccion());
        existente.setTelefono(c.getTelefono());
        existente.setEmail(c.getEmail());
        existente.setEstado(c.getEstado());
        return ResponseEntity.ok(service.guardar(existente));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}