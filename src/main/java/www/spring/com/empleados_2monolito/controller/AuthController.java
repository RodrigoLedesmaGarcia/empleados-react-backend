package www.spring.com.empleados_2monolito.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import www.spring.com.empleados_2monolito.entity.UsuarioLogin;
import www.spring.com.empleados_2monolito.repository.UsuarioLoginRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class AuthController {

    private final UsuarioLoginRepository usuarioRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UsuarioLoginRepository usuarioRepo, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"/login", "/login/"})
    public String loginView() {
        return "login";
    }


    @PostMapping("/auth/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        UsuarioLogin usuario = usuarioRepo.findByUsername(username);

        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }

        session.setAttribute("usuario", usuario);

        // si usaste redirectAfterLogin en el interceptor:
        String redirect = (String) session.getAttribute("redirectAfterLogin");
        if (redirect != null) {
            session.removeAttribute("redirectAfterLogin");
            return "redirect:" + redirect;
        }

        return "redirect:/ui/employees";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
