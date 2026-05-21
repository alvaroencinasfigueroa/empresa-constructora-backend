package bo.constructora.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles_usuario")
@Data
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;
}