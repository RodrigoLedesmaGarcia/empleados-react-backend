package www.spring.com.empleados_2monolito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import www.spring.com.empleados_2monolito.entity.UsuarioLogin;

public interface UsuarioLoginRepository extends JpaRepository<UsuarioLogin, Long> {

    UsuarioLogin findByUsername(String username);
}
