package bo.constructora.backend.controller;

import bo.constructora.backend.entity.BitacoraLog;
import bo.constructora.backend.service.BitacoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BitacoraController {

    private final BitacoraService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<BitacoraLog> listar(@RequestParam(defaultValue = "100") int limite) {
        return service.listarRecientes(limite);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<BitacoraLog> porUsuario(@PathVariable Integer idUsuario,
                                        @RequestParam(defaultValue = "50") int limite) {
        return service.listarPorUsuario(idUsuario, limite);
    }

    @GetMapping("/tabla/{tabla}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<BitacoraLog> porTabla(@PathVariable String tabla,
                                      @RequestParam(defaultValue = "50") int limite) {
        return service.listarPorTabla(tabla, limite);
    }
}