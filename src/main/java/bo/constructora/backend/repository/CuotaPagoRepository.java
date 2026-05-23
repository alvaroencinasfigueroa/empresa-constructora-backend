package bo.constructora.backend.repository;

import bo.constructora.backend.entity.CuotaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CuotaPagoRepository extends JpaRepository<CuotaPago, Integer> {

    List<CuotaPago> findByContrato_IdContratoOrderByNumeroCuota(Integer idContrato);

    // Cuotas vencidas: fecha_vencimiento < hoy y estado = Pendiente
    @Query("""
        SELECT c FROM CuotaPago c
        WHERE c.contrato.idContrato = :idContrato
          AND c.estado = 'Pendiente'
          AND c.fechaVencimiento < CURRENT_DATE
        """)
    List<CuotaPago> findVencidasByContrato(@Param("idContrato") Integer idContrato);

    // Marcar como Vencidas las cuotas que ya pasaron su fecha
    @Modifying
    @Query("""
        UPDATE CuotaPago c SET c.estado = 'Vencido'
        WHERE c.estado = 'Pendiente'
          AND c.fechaVencimiento < CURRENT_DATE
        """)
    int marcarVencidas();

    boolean existsByContrato_IdContrato(Integer idContrato);
}
