package bo.constructora.backend.repository;

import bo.constructora.backend.entity.CuotaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CuotaPagoRepository extends JpaRepository<CuotaPago, Integer> {

    @Query("SELECT c FROM CuotaPago c LEFT JOIN FETCH c.pago WHERE c.contrato.idContrato = :idContrato ORDER BY c.numeroCuota")
    List<CuotaPago> findByContrato_IdContratoOrderByNumeroCuota(@Param("idContrato") Integer idContrato);

    @Query("""
        SELECT c FROM CuotaPago c
        WHERE c.contrato.idContrato = :idContrato
          AND c.estado = 'Pendiente'
          AND c.fechaVencimiento < CURRENT_DATE
        """)
    List<CuotaPago> findVencidasByContrato(@Param("idContrato") Integer idContrato);

    @Modifying
    @Query("""
        UPDATE CuotaPago c SET c.estado = 'Vencido'
        WHERE c.estado = 'Pendiente'
          AND c.fechaVencimiento < CURRENT_DATE
        """)
    int marcarVencidas();

    boolean existsByContrato_IdContrato(Integer idContrato);
}