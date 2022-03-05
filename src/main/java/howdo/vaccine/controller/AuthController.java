package howdo.vaccine.controller;

import howdo.vaccine.model.User;
import howdo.vaccine.model.UserRegistrationForm;
import howdo.vaccine.repository.UserRepository;
import howdo.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerGet(@ModelAttribute User user) {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerPost(HttpServletResponse response, @Valid @ModelAttribute UserRegistrationForm user) throws IOException {
        userService.createUser(user.getPpsNumber(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getPhoneNumber(), user.getEmailAddress(), user.getNationality());
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
