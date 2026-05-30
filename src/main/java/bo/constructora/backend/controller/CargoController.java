package bo.constructora.backend.controller;

import bo.constructora.backend.entity.Cargo;
import bo.constructora.backend.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CargoController {

    private final CargoRepository repo;

    @GetMapping
    public List<Cargo> listar() { return repo.findAll(); }
}