package www.spring.com.empleados_2monolito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.spring.com.empleados_2monolito.entity.EmployeeDTO;

import java.time.LocalDate;


@Repository
public interface EmpleadoRepository extends JpaRepository<EmployeeDTO, Integer> {


}
