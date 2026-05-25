package bo.constructora.backend.controller;

import bo.constructora.backend.entity.TipoContrato;
import bo.constructora.backend.repository.TipoContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-contrato")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TipoContratoController {

    private final TipoContratoRepository repo;

    @GetMapping
    public List<TipoContrato> listar() {
        return repo.findAll();
    }
}