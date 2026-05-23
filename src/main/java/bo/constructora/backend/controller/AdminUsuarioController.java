package bo.constructora.backend.controller;

import bo.constructora.backend.dto.CrearUsuarioEmpleadoRequest;
import bo.constructora.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearUsuarioEmpleado(@RequestBody CrearUsuarioEmpleadoRequest req) {
        usuarioService.crearUsuarioEmpleado(req);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario creado exitosamente"));
    }
}