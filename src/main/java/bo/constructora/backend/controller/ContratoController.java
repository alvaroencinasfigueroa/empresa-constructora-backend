package bo.constructora.backend.controller;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService service;

    // Solo ADMIN y GERENTE pueden crear contratos
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ResponseEntity<ContratoResponse> crear(@RequestBody ContratoRequest req) {
        return ResponseEntity.ok(service.crear(req));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ADMINISTRATIVO')")
    public List<ContratoResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ADMINISTRATIVO')")
    public ResponseEntity<ContratoResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Portal cliente: ver solo sus propios contratos
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ADMINISTRATIVO','CLIENTE')")
    public List<ContratoResponse> porCliente(@PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ResponseEntity<ContratoResponse> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }
}
