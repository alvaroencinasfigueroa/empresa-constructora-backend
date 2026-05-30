package bo.constructora.backend.repository;

import bo.constructora.backend.entity.Empleado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @EntityGraph(attributePaths = {"cargo"})
    List<Empleado> findAll();

    @EntityGraph(attributePaths = {"cargo"})
    Optional<Empleado> findById(Integer id);
}