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

    //add a new Appointment object and a list of VaccinationCentres to the model
    @ModelAttribute
    public void addAttributes(Model model)
    {
        List<VaccinationCentre> locations = centreRepository.findAll();
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("locations", locations);
    }

    @GetMapping("/appointments")
    public String registerGet(@ModelAttribute User user) {return "registerVaccineAppt";}


    @PostMapping(value = "/appointments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void appointmentsPost(HttpServletResponse response, HttpServletRequest request, @Valid @ModelAttribute Appointment appointment) throws IOException, ParseException {
        //parse the data from the form
        //String dateString = request.getParameter("date");           //date is in form yyyy-mm-dd
        String locationString = request.getParameter("location");   //contains the ID of the VaccinationCentre
        User user = userService.getCurrentUser();

        //find the right VaccinationCentre
        VaccinationCentre location = centreRepository.getOne(Long.parseLong(locationString));
        long id = appointmentRepository.findAll().size() + 1;

        System.out.println("\n\n\n"+"TEST"+"\n\n\n");
        System.out.println("\n\n\n"+request.getParameter("user")+"\n\n\n");

        //the appointment object contains a date and a vaccination centre from the form
        appointment.setId(id);
        appointment.setUser(user);
        appointment.setLocation(location);
        appointmentRepository.save(appointment);

        response.sendRedirect("/home");
    }
}
