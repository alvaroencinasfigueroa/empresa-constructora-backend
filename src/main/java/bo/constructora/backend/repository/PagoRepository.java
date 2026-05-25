package bo.constructora.backend.repository;

import bo.constructora.backend.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Query("SELECT p FROM Pago p JOIN FETCH p.contrato c JOIN FETCH c.cliente JOIN FETCH c.proyecto JOIN FETCH p.metodoPago WHERE c.idContrato = :idContrato ORDER BY p.fechaPago DESC")
    List<Pago> findByContratoConFetch(@Param("idContrato") Integer idContrato);

    @Query("SELECT p FROM Pago p JOIN FETCH p.contrato c JOIN FETCH c.cliente JOIN FETCH c.proyecto JOIN FETCH p.metodoPago WHERE p.idPago = :id")
    Optional<Pago> findByIdConFetch(@Param("id") Integer id);
}