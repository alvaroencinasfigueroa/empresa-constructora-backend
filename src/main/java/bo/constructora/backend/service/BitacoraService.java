package bo.constructora.backend.service;

import bo.constructora.backend.entity.BitacoraLog;
import bo.constructora.backend.repository.BitacoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BitacoraService {

    private final BitacoraRepository repo;

    @Transactional
    public BitacoraLog registrar(Integer idUsuario, String accion, String tabla, String detalle) {
        BitacoraLog log = new BitacoraLog();
        log.setIdUsuario(idUsuario);
        log.setAccion(accion);
        log.setTablaAfectada(tabla);
        log.setFechaHora(LocalDateTime.now());
        log.setDetalle(detalle);
        return repo.save(log);
    }

    public List<BitacoraLog> listarRecientes(int limite) {
        return repo.findAllByOrderByFechaHoraDesc(PageRequest.of(0, limite));
    }

    public List<BitacoraLog> listarPorUsuario(Integer idUsuario, int limite) {
        return repo.findByIdUsuarioOrderByFechaHoraDesc(idUsuario, PageRequest.of(0, limite));
    }

    public List<BitacoraLog> listarPorTabla(String tabla, int limite) {
        return repo.findByTablaAfectadaIgnoreCaseOrderByFechaHoraDesc(tabla, PageRequest.of(0, limite));
    }
}