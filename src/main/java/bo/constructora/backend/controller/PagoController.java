package bo.constructora.backend.controller;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    // Registrar pago (y marcar cuota automáticamente si se manda idCuota)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR')")
    public ResponseEntity<PagoResponse> registrar(@RequestBody PagoRequest req) {
        return ResponseEntity.ok(service.registrar(req));
    }

    // Ver pagos de un contrato (el cliente también puede ver los suyos)
    @GetMapping("/contrato/{idContrato}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','CLIENTE')")
    public List<PagoResponse> porContrato(@PathVariable Integer idContrato) {
        return service.listarPorContrato(idContrato);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','CLIENTE')")
    public ResponseEntity<PagoResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Anular pago (revierte cuota a Pendiente)
    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<PagoResponse> anular(@PathVariable Integer id) {
        return ResponseEntity.ok(service.anular(id));
    }
}