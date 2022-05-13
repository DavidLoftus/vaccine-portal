package howdo.vaccine.controller;

import howdo.vaccine.enums.Nationality;
import howdo.vaccine.model.UserRegistrationForm;
import howdo.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerGet(@ModelAttribute("user") UserRegistrationForm user) {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerPost(HttpServletResponse response, @Valid @ModelAttribute("user") UserRegistrationForm form) throws IOException {

        userService.createUser(form.getPpsNumber(),
                form.getPassword(),
                form.getFirstName(),
                form.getLastName(),
                form.getDateOfBirth(),
                form.getPhoneNumber(),
                form.getEmailAddress(),
                form.getNationality());

        response.sendRedirect("/");
    }

    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @Value("${portal.admin.password}")
    private String adminPassword = "password";

    @PostConstruct
    public void init() {
        try {
            userService.getUser("admin");
        } catch (UsernameNotFoundException e) {
            userService.createUser("admin", "password", "John", "Smith",
                    new Date(), "0123456789", "admin@localhost", Nationality.IRISH,
                    Set.of("USER", "ADMIN"));
        }
    }

}
