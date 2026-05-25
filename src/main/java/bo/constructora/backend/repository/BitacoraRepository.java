package bo.constructora.backend.repository;

import bo.constructora.backend.entity.BitacoraLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BitacoraRepository extends JpaRepository<BitacoraLog, Integer> {
    List<BitacoraLog> findByIdUsuarioOrderByFechaHoraDesc(Integer idUsuario, Pageable pageable);
    List<BitacoraLog> findByTablaAfectadaIgnoreCaseOrderByFechaHoraDesc(String tabla, Pageable pageable);
    List<BitacoraLog> findAllByOrderByFechaHoraDesc(Pageable pageable);
}