package www.spring.com.empleados_2monolito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Empleados2MonolitoApplication {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("12345"));
        SpringApplication.run(Empleados2MonolitoApplication.class, args);
    }

}
