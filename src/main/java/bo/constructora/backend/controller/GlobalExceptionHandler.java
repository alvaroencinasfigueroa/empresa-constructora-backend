package bo.constructora.backend.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de integridad de BD (NOT NULL, FK, unique, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause().getMessage();

        // Mensajes amigables según el campo que falle
        if (msg.contains("Proyecto.cliente") || msg.contains("id_cliente")) {
            return ResponseEntity.badRequest().body("El cliente es obligatorio y no puede estar vacío");
        }
        if (msg.contains("unique") || msg.contains("duplicada")) {
            return ResponseEntity.badRequest().body("Ya existe un registro con este valor único");
        }
        return ResponseEntity.badRequest().body("Error de datos: " + msg);
    }

    // Maneja errores de validación lógica (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Manejo genérico para otros errores no esperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        // En producción, registra el error con logger en lugar de mostrarlo
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body("Error interno del servidor");
    }
}