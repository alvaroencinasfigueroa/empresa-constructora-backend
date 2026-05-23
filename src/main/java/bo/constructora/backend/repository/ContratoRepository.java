package bo.constructora.backend.repository;

// ── ContratoRepository ───────────────────────────────────────────────────────
import bo.constructora.backend.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {

    List<Contrato> findByCliente_IdCliente(Integer idCliente);

    List<Contrato> findByProyecto_IdProyecto(Integer idProyecto);

    @Query("""
        SELECT COALESCE(SUM(p.monto), 0)
        FROM Pago p
        WHERE p.contrato.idContrato = :idContrato
          AND p.estado = 'Confirmado'
        """)
    java.math.BigDecimal sumPagosConfirmados(@Param("idContrato") Integer idContrato);
}
