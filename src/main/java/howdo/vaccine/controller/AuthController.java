package howdo.vaccine.controller;

import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerGet(@ModelAttribute User user) {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerPost(HttpServletResponse response, @Valid @ModelAttribute User user) throws IOException {
        // TODO: this is insecure since client could register authorities
        //  use a separate UserDTO class for registration
        user.setAuthorities(Set.of("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        response.sendRedirect("/home");
    }

    @Value("${portal.admin.password}")
    private String adminPassword = "password";

    @PostConstruct
    public void init() {
        try {
            userService.getUser("admin");
        } catch (UsernameNotFoundException e) {
            userService.createUser("admin", "password", "John", "Smith",
                    new Date(), "0123456789", "admin@localhost", "Irish",
                    Set.of("USER", "ADMIN"));
        }
    }

}
