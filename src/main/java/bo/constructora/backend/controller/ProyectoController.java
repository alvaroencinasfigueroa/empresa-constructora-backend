package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService service;

    @GetMapping
    public List<Proyecto> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Proyecto> crear(@RequestBody Proyecto p) {
        return ResponseEntity.ok(service.guardar(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Integer id, @RequestBody Proyecto p) {
        p.setIdProyecto(id);
        return ResponseEntity.ok(service.guardar(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}