package bo.constructora.backend.controller;

import bo.constructora.backend.dto.PagosDTO.*;
import bo.constructora.backend.service.CuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuotas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CuotaController {

    private final CuotaService service;

    // Generar plan de cuotas para un contrato
    @PostMapping("/generar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR')")
    public ResponseEntity<List<CuotaResponse>> generar(@RequestBody GenerarCuotasRequest req) {
        return ResponseEntity.ok(service.generarPlan(req));
    }

    // Ver cuotas de un contrato (cliente también puede ver)
    @GetMapping("/contrato/{idContrato}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','CLIENTE')")
    public List<CuotaResponse> porContrato(@PathVariable Integer idContrato) {
        return service.listarPorContrato(idContrato);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','CLIENTE')")
    public ResponseEntity<CuotaResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Editar monto o fecha de una cuota (renegociación)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<CuotaResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody CuotaResponse req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    // Eliminar plan completo (solo si ninguna cuota está pagada)
    @DeleteMapping("/contrato/{idContrato}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<Void> eliminarPlan(@PathVariable Integer idContrato) {
        service.eliminarPlan(idContrato);
        return ResponseEntity.noContent().build();
    }

    // Resumen financiero del contrato
    @GetMapping("/resumen/{idContrato}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CONTADOR','CLIENTE')")
    public ResponseEntity<ResumenFinancieroResponse> resumen(@PathVariable Integer idContrato) {
        return ResponseEntity.ok(service.resumen(idContrato));
    }
}