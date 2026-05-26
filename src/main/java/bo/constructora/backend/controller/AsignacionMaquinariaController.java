package bo.constructora.backend.controller;

import bo.constructora.backend.entity.AsignacionMaquinaria;
import bo.constructora.backend.service.AsignacionMaquinariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones-maquinaria")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AsignacionMaquinariaController {

    private final AsignacionMaquinariaService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','LOGISTICA','INGENIERO')")
    public List<AsignacionMaquinaria> listar(
            @RequestParam(required = false) Integer idProyecto,
            @RequestParam(required = false) Integer idMaquinaria,
            @RequestParam(required = false) Integer idEmpleado) {

        if (idProyecto != null) {
            return service.listarPorProyecto(idProyecto);
        }
        if (idMaquinaria != null) {
            return service.listarPorMaquinaria(idMaquinaria);
        }
        if (idEmpleado != null) {
            return service.listarPorEmpleado(idEmpleado);
        }
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','LOGISTICA','INGENIERO')")
    public ResponseEntity<AsignacionMaquinaria> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','LOGISTICA')")
    public ResponseEntity<AsignacionMaquinaria> crear(@RequestBody AsignacionMaquinaria a) {
        return ResponseEntity.ok(service.guardar(a));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','LOGISTICA')")
    public ResponseEntity<AsignacionMaquinaria> actualizar(@PathVariable Integer id, @RequestBody AsignacionMaquinaria a) {
        return ResponseEntity.ok(service.actualizar(id, a));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
