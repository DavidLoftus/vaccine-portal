package howdo.vaccine.controller;

import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping({"/stats"})
    public String viewHomePage(Model model) {
        int total = userRepository.userTotal();
        model.addAttribute("total", total);
        return "stats";
    }


}
