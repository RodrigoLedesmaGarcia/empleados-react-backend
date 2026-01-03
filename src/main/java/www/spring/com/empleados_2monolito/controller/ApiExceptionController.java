package www.spring.com.empleados_2monolito.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> handBadRequest(IllegalAccessException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
