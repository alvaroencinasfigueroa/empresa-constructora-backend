package bo.constructora.backend.repository;

import bo.constructora.backend.entity.AsignacionMaquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsignacionMaquinariaRepository extends JpaRepository<AsignacionMaquinaria, Integer> {

    List<AsignacionMaquinaria> findByProyecto_IdProyecto(Integer idProyecto);

    List<AsignacionMaquinaria> findByMaquinaria_IdMaquinaria(Integer idMaquinaria);

    List<AsignacionMaquinaria> findByEmpleadoResponsable_IdEmpleado(Integer idEmpleado);
}