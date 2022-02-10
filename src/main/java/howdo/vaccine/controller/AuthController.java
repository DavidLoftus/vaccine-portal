package howdo.vaccine.controller;

import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String registerGet() {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerPost(HttpServletResponse response, @Valid User user) throws IOException {
        userRepository.save(user);

        response.sendRedirect("/home");
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}
