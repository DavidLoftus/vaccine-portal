package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.repository.UserRepository;
import howdo.vaccine.repository.VaccineAptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class VaccineController {

    @Autowired
    VaccineAptRepository vaccineAptRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/appointments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void appointmentsPost(HttpServletResponse response, @Valid @ModelAttribute Appointment appointment) throws IOException {

        //TODO

        vaccineAptRepository.save(appointment);

        response.sendRedirect("/home");
    }
}
