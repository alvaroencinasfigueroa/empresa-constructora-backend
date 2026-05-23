package bo.constructora.backend.service;

import bo.constructora.backend.dto.CrearUsuarioEmpleadoRequest;
import bo.constructora.backend.entity.*;
import bo.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;
    private final EmpleadoRepository empleadoRepo;
    private final RolUsuarioRepository rolUsuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol().getNombre()))
        );
    }

    public Usuario findByUsername(String username) {
        return usuarioRepo.findByUsername(username).orElseThrow();
    }

    @Transactional
    public void crearUsuarioEmpleado(CrearUsuarioEmpleadoRequest req) {
        Empleado empleado = empleadoRepo.findById(req.getIdEmpleado())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        RolUsuario rol = rolUsuarioRepo.findById(req.getIdRol())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setEmpleado(empleado);
        u.setRol(rol);
        u.setEstado(true);
        usuarioRepo.save(u);
    }
}