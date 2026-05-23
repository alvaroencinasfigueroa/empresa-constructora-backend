package bo.constructora.backend.repository;

// ── TipoContratoRepository ───────────────────────────────────────────────────
import bo.constructora.backend.entity.TipoContrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoContratoRepository extends JpaRepository<TipoContrato, Integer> {
}

// ── MetodoPagoRepository ─────────────────────────────────────────────────────
// (archivo separado: MetodoPagoRepository.java)
//
// import bo.constructora.backend.entity.MetodoPago;
// import org.springframework.data.jpa.repository.JpaRepository;
// public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {}
