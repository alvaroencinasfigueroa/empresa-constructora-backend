package bo.constructora.backend.repository;

import bo.constructora.backend.entity.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoClienteRepository extends JpaRepository<TipoCliente, Integer> {
}