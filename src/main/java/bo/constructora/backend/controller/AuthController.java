package bo.constructora.backend.controller;

import bo.constructora.backend.dto.ClienteRegistroRequest;
import bo.constructora.backend.entity.Usuario;
import bo.constructora.backend.security.JwtUtil;
import bo.constructora.backend.service.ClienteService;
import bo.constructora.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            // Autenticar
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            body.get("username"),
                            body.get("password")
                    )
            );

            Usuario u = usuarioService.findByUsername(body.get("username"));
            String token = jwtUtil.generateToken(u.getUsername(), u.getRol().getNombre());

            // Construir el nombre completo según el tipo de usuario
            String nombreCompleto = "";
            if (u.getEmpleado() != null) {
                nombreCompleto = u.getEmpleado().getNombre() + " " + u.getEmpleado().getApellido();
            } else if (u.getCliente() != null) {
                nombreCompleto = u.getCliente().getNombre();
                if (u.getCliente().getApellido() != null) {
                    nombreCompleto += " " + u.getCliente().getApellido();
                }
            }

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", u.getUsername(),
                    "rol", u.getRol().getNombre(),
                    "nombre", nombreCompleto
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRegistroRequest req) {
        clienteService.registrarCliente(req, passwordEncoder);
        return ResponseEntity.ok(Map.of("mensaje", "Cliente registrado exitosamente"));
    }
}