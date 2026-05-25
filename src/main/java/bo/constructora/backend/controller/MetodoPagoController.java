package bo.constructora.backend.controller;

import bo.constructora.backend.entity.MetodoPago;
import bo.constructora.backend.repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MetodoPagoController {

    private final MetodoPagoRepository repo;

    @GetMapping
    public List<MetodoPago> listar() {
        return repo.findAll();
    }
}