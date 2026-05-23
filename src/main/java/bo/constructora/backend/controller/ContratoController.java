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

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<ContratoResponse> crear(@RequestBody ContratoRequest req) {
        return ResponseEntity.ok(service.crear(req));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR')")
    public List<ContratoResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR')")
    public ResponseEntity<ContratoResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','CLIENTE')")
    public List<ContratoResponse> porCliente(@PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<ContratoResponse> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }
}