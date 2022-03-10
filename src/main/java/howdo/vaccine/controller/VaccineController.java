package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.repository.UserRepository;
import howdo.vaccine.repository.VaccineAptRepository;
import howdo.vaccine.repository.VaccineCentreRepository;
import howdo.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.*;
import java.util.Date;
import java.util.List;

@Controller
public class VaccineController {

    @Autowired
    VaccineAptRepository appointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VaccineCentreRepository centreRepository;

    @Autowired
    UserService userService;

    //add a list of VaccinationCentres to the model
    @ModelAttribute
    public void addAttributes(Model model) {
        List<VaccinationCentre> locations = centreRepository.findAll();
        model.addAttribute("locations", locations);
    }

    @GetMapping("/appointments")
    public String registerGet(@ModelAttribute Appointment appointment) {
        return "registerVaccineAppt";
    }


    @PostMapping(value = "/appointments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void appointmentsPost(HttpServletResponse response, HttpServletRequest request, @Valid @ModelAttribute Appointment appointment) throws IOException, ParseException {
        //parse the data from the form
        User user = userService.getCurrentUser();
        String locationString = request.getParameter("location");   //contains the ID of the VaccinationCentre
        VaccinationCentre location = centreRepository.getOne(Long.parseLong(locationString));

        long id = appointmentRepository.findAll().size() + 1;

        //the appointment object contains a date from the form
        appointment.setId(id);
        appointment.setUser(user);
        appointment.setLocation(location);
        appointmentRepository.save(appointment);

        response.sendRedirect("/");
    }

    @GetMapping("/edit")
    public String editGet(@ModelAttribute User user) {
        return "editVaccineAppt";
    }


    @PostMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void editPost(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable long appointmentId) throws IOException, ParseException {

        //convert this appointment into a dose

        //book a second appointment if this is the first one

        //delete this appointment

        response.sendRedirect("/home");
    }
}
