package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Maquinaria;
import bo.constructora.backend.service.MaquinariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/maquinaria")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MaquinariaController {

    private final MaquinariaService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','LOGISTICA','INGENIERO')")
    public List<Maquinaria> listar(@RequestParam(required = false) String nombre,
                                   @RequestParam(required = false) String estado) {
        if (nombre != null && !nombre.isBlank()) {
            return service.buscarPorNombre(nombre);
        }
        if (estado != null && !estado.isBlank()) {
            return service.listarPorEstado(estado);
        }
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','LOGISTICA','INGENIERO')")
    public ResponseEntity<Maquinaria> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','LOGISTICA')")
    public ResponseEntity<Maquinaria> crear(@RequestBody Maquinaria m) {
        return ResponseEntity.ok(service.guardar(m));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','LOGISTICA')")
    public ResponseEntity<Maquinaria> actualizar(@PathVariable Integer id, @RequestBody Maquinaria m) {
        return ResponseEntity.ok(service.actualizar(id, m));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}