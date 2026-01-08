package www.spring.com.empleados_2monolito.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import www.spring.com.empleados_2monolito.entity.EmployeeDTO;
import www.spring.com.empleados_2monolito.service.EmpleadoServiceImpl;

import java.time.LocalDate;

@Controller
@RequestMapping("/ui/employees")
public class EmpleadoViewController {

    private final EmpleadoServiceImpl service;

    public EmpleadoViewController(EmpleadoServiceImpl service) {
        this.service = service;
    }


    @GetMapping({"", "/", " "})
    public String view(){
        return "employees";
    }

    private String normalize(String str){
        if (str == null){
            return null;
        }
        str = str.trim();

        return str.isEmpty() ? null : str;
    }


    @PostMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) Integer empNo,
            @RequestParam(required = false) LocalDate birtDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) LocalDate hireDate,
            @RequestParam(required = false) String deptNo,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            Model model
    ){

        firstName = normalize(firstName);
        lastName = normalize(lastName);
        deptNo = normalize(deptNo);
        gender = normalize(gender);


        Page<EmployeeDTO> result = service.buscarConFiltros(
                empNo,
                birtDate,
                firstName,
                lastName,
                gender,
                hireDate,
                deptNo,
                fromDate,
                toDate,
                page,
                size
        );

        model.addAttribute("results", result);

        model.addAttribute("empNo", empNo);
        model.addAttribute("birthDate", birtDate);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("gender", gender);
        model.addAttribute("hireDate", hireDate);
        model.addAttribute("deptNo", deptNo);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "employees";

    }
}
