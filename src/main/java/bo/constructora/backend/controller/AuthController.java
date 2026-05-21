package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Usuario;
import bo.constructora.backend.security.JwtUtil;
import bo.constructora.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    //private final AuthenticationManager authManager;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            System.out.println("Intentando login con: " + body.get("username"));
            System.out.println("Password recibido: " + body.get("password"));

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashDeBD = usuarioService.findByUsername(body.get("username")).getPasswordHash();
            boolean coincide = encoder.matches(body.get("password"), hashDeBD);
            System.out.println("Hash en BD: " + hashDeBD);
            System.out.println("¿Coincide 123456 con el hash?: " + coincide);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            body.get("username"),
                            body.get("password")
                    )
            );
            Usuario u = usuarioService.findByUsername(body.get("username"));
            String token = jwtUtil.generateToken(u.getUsername(), u.getRol().getNombre());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", u.getUsername(),
                    "rol", u.getRol().getNombre(),
                    "nombre", u.getEmpleado().getNombre() + " " + u.getEmpleado().getApellido()
            ));
        } catch (AuthenticationException e) {
            System.out.println("Error de autenticación: " + e.getMessage() + " - " + e.getClass().getName());
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }
}