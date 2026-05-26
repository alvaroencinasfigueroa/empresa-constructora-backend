package bo.constructora.backend.repository;

import bo.constructora.backend.entity.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaquinariaRepository extends JpaRepository<Maquinaria, Integer> {

    List<Maquinaria> findByNombreContainingIgnoreCase(String nombre);

    List<Maquinaria> findByEstado(String estado);
}