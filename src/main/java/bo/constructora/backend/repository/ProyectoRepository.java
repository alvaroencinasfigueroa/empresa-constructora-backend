package bo.constructora.backend.repository;

import bo.constructora.backend.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByCliente_IdCliente(Integer idCliente);
}