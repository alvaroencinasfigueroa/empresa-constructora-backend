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

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashJava = encoder.encode("123456");
            System.out.println("HASH JAVA: " + hashJava);

            // Prueba 1: hash hardcodeado
            String hashHardcodeado = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.";
            System.out.println("Prueba hardcodeado: " + encoder.matches("123456", hashHardcodeado));

            // Prueba 2: hash de BD
            String hashBD = usuarioService.findByUsername(body.get("username")).getPasswordHash();
            System.out.println("Hash BD bytes: " + java.util.Arrays.toString(hashBD.getBytes()));
            System.out.println("Hash hard bytes: " + java.util.Arrays.toString(hashHardcodeado.getBytes()));
            System.out.println("¿Son iguales?: " + hashBD.equals(hashHardcodeado));

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