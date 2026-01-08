package www.spring.com.empleados_2monolito.service;

import org.springframework.data.domain.Page;
import www.spring.com.empleados_2monolito.entity.EmployeeDTO;

import java.time.LocalDate;

public interface EmpleadoService {

    Page<EmployeeDTO> buscarConFiltros(
            Integer empNo,
            LocalDate birthDate,
            String firstName,
            String lastName,
            String gender,
            LocalDate hireDate,
            String deptNo,
            LocalDate fromDate,
            LocalDate toDate,
            int page,
            int size
    );
}
