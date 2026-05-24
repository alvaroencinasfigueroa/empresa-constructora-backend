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
            // ---------- DIAGNÓSTICO TEMPORAL ----------
            Usuario u = usuarioService.findByUsername(body.get("username"));
            System.out.println(">>> Hash en BD: " + u.getPasswordHash());
            System.out.println(">>> Contraseña ingresada: '" + body.get("password") + "'");

            boolean coincide = passwordEncoder.matches(body.get("password"), u.getPasswordHash());
            System.out.println(">>> ¿Coinciden las contraseñas?: " + coincide);
            // -----------------------------------------

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            body.get("username"),
                            body.get("password")
                    )
            );

            Usuario u2 = usuarioService.findByUsername(body.get("username"));
            String token = jwtUtil.generateToken(u2.getUsername(), u2.getRol().getNombre());

            // Construir el nombre completo según el tipo de usuario
            String nombreCompleto = "";
            if (u2.getEmpleado() != null) {
                nombreCompleto = u2.getEmpleado().getNombre() + " " + u2.getEmpleado().getApellido();
            } else if (u2.getCliente() != null) {
                nombreCompleto = u2.getCliente().getNombre();
                if (u2.getCliente().getApellido() != null) {
                    nombreCompleto += " " + u2.getCliente().getApellido();
                }
            }

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", u2.getUsername(),
                    "rol", u2.getRol().getNombre(),
                    "nombre", nombreCompleto
            ));
        } catch (AuthenticationException e) {
            System.out.println(">>> ERROR de autenticación: " + e.getClass().getName());
            System.out.println(">>> Mensaje: " + e.getMessage());
            e.printStackTrace(); // Esto imprime la traza completa en consola
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