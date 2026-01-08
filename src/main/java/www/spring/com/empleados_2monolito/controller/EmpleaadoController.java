package www.spring.com.empleados_2monolito.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import www.spring.com.empleados_2monolito.entity.EmployeeDTO;
import www.spring.com.empleados_2monolito.service.EmpleadoServiceImpl;

import java.time.LocalDate;

//@CrossOrigin(origins = "http://localhost:5173")

//@RestController
@Controller
@RequestMapping("/empleados")
public class EmpleaadoController {

    private final EmpleadoServiceImpl service;

    public EmpleaadoController(EmpleadoServiceImpl service) {
        this.service = service;
    }



    @GetMapping("/buscar")
    public ResponseEntity<Page<EmployeeDTO>> buscar(
            @RequestParam(required = false) Integer empNo,
            @RequestParam(required = false)LocalDate birthDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) LocalDate hireDate,
            @RequestParam(required = false) String deptNo,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate tpDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
            ){
        Page<EmployeeDTO> result = service.buscarConFiltros(
                empNo,
                birthDate,
                firstName,
                lastName,
                gender,
                hireDate,
                deptNo,
                fromDate
                ,tpDate,
                page,
                size
                );

        return ResponseEntity.ok(result);
    }
}
