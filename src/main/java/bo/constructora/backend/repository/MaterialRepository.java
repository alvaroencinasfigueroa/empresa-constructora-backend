package bo.constructora.backend.repository;

import bo.constructora.backend.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    List<Material> findByNombreContainingIgnoreCase(String nombre);
}