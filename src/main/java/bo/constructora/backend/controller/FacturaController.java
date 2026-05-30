package bo.constructora.backend.controller;

import bo.constructora.backend.service.FacturaService;
import bo.constructora.backend.service.FacturaService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','SECRETARIA')")
    public List<FacturaResponse> listar() {
        return service.listarTodas();
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','SECRETARIA','CLIENTE')")
    public List<FacturaResponse> porCliente(@PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CONTADOR','SECRETARIA')")
    public ResponseEntity<?> emitir(@RequestBody FacturaRequest req) {
        try {
            return ResponseEntity.ok(service.emitir(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CONTADOR')")
    public ResponseEntity<?> marcarPagada(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.marcarPagada(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}