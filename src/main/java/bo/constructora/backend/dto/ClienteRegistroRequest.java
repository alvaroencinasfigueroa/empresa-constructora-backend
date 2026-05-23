package bo.constructora.backend.dto;

import lombok.Data;

@Data
public class ClienteRegistroRequest {
    private String nombre;
    private String apellido;
    private String rucNit;
    private String direccion;
    private String telefono;
    private String email;
    private String username;
    private String password;
}