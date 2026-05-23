package bo.constructora.backend.repository;

import bo.constructora.backend.entity.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {
    Optional<RolUsuario> findByNombre(String nombre);
}