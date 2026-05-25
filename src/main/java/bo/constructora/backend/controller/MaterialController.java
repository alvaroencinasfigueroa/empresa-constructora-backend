package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Material;
import bo.constructora.backend.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/materiales")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','LOGISTICA','INGENIERO')")
    public List<Material> listar(@RequestParam(required = false) String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return service.buscar(nombre);
        }
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','LOGISTICA','INGENIERO')")
    public ResponseEntity<Material> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','LOGISTICA')")
    public ResponseEntity<Material> crear(@RequestBody Material m) {
        return ResponseEntity.ok(service.guardar(m));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','LOGISTICA')")
    public ResponseEntity<Material> actualizar(@PathVariable Integer id, @RequestBody Material m) {
        return ResponseEntity.ok(service.actualizar(id, m));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}