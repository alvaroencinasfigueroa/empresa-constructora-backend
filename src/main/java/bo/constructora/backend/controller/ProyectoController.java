/*package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','INGENIERO','CONTADOR','SECRETARIA')")
    public List<Proyecto> listar() {
        return service.listarTodos();
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CLIENTE')")
    public List<Proyecto> porCliente(@PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','INGENIERO','CONTADOR','CLIENTE')")
    public ResponseEntity<Proyecto> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<Proyecto> crear(@RequestBody Proyecto p) {
        return ResponseEntity.ok(service.guardar(p));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Integer id, @RequestBody Proyecto p) {
        p.setIdProyecto(id);
        return ResponseEntity.ok(service.guardar(p));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}*/

package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Proyecto;
import bo.constructora.backend.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','INGENIERO','CONTADOR','SECRETARIA')")
    public List<Proyecto> listar() {
        return service.listarTodos();
    }

    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','CLIENTE')")
    public List<Proyecto> porCliente(@PathVariable Integer idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA','INGENIERO','CONTADOR','CLIENTE')")
    public ResponseEntity<Proyecto> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<Proyecto> crear(@RequestBody Proyecto p) {
        try {
            Proyecto creado = service.crear(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // ← 400 en lugar de 500
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','DIRECTOR_OBRA')")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Integer id, @RequestBody Proyecto p) {
        try {
            return ResponseEntity.ok(service.actualizar(id, p));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // ← 400 en lugar de 500
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}