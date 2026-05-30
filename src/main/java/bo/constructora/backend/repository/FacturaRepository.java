package bo.constructora.backend.repository;

import bo.constructora.backend.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    @Query("SELECT f FROM Factura f JOIN FETCH f.contrato c JOIN FETCH c.proyecto JOIN FETCH f.cliente ORDER BY f.fechaEmision DESC")
    List<Factura> findAllConFetch();

    @Query("SELECT f FROM Factura f JOIN FETCH f.contrato c JOIN FETCH c.proyecto JOIN FETCH f.cliente WHERE f.cliente.idCliente = :idCliente ORDER BY f.fechaEmision DESC")
    List<Factura> findByClienteConFetch(@Param("idCliente") Integer idCliente);

    @Query("SELECT f FROM Factura f JOIN FETCH f.contrato c JOIN FETCH c.proyecto JOIN FETCH f.cliente WHERE f.idFactura = :id")
    Optional<Factura> findByIdConFetch(@Param("id") Integer id);
}