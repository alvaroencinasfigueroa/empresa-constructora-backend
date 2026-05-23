package bo.constructora.backend.dto;

import lombok.Data;

@Data
public class CrearUsuarioEmpleadoRequest {
    private Integer idEmpleado;
    private String username;
    private String password;
    private Integer idRol;
}