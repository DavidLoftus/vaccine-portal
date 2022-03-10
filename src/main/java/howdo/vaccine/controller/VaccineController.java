package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.model.VaccineDose;
import howdo.vaccine.repository.UserRepository;
import howdo.vaccine.repository.VaccineAptRepository;
import howdo.vaccine.repository.VaccineCentreRepository;
import howdo.vaccine.repository.VaccineDoseRepository;
import howdo.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.util.Calendar;
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
    VaccineDoseRepository doseRepository;

    @Autowired
    UserService userService;

    private void bookAppointment(User user, VaccinationCentre location, Date date)
    {
        //find the entry with the largest id and add one to it; prevents repeat ids without needing to update a static variable
        //ternary operator prevents NPEs
        long id = (appointmentRepository.findAll().size() != 0) ?
                appointmentRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0).getId() + 1
                : 1;
        Appointment appointment = new Appointment();

        appointment.setId(id);
        appointment.setUser(user);
        appointment.setLocation(location);
        appointment.setAppointmentTime(date);
        appointmentRepository.save(appointment);
    }

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

        bookAppointment(user, location, appointment.getAppointmentTime());

        response.sendRedirect("/");
    }

    @GetMapping("/edit")
    public String editGet(@ModelAttribute User user) {
        return "editVaccineAppt";
    }


    @PostMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void editPost(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable long id) throws IOException, ParseException {

        //convert this appointment into a dose
        Appointment app = appointmentRepository.getOne(id);
        VaccineDose dose = new VaccineDose();

        long doseID = (doseRepository.findAll().size() != 0) ?
                doseRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0).getId() + 1 :
                1;
        dose.setId(doseID);
        dose.setDose(app.getUser().getDoses().isEmpty() ? 1 : 2);
        dose.setUser(app.getUser());
        dose.setDate(app.getAppointmentTime());
        dose.setVaccineType(request.getParameter("type"));
        doseRepository.save(dose);

        //book a second appointment if this is the first one
        if (dose.getDose() == 1)
        {
            //book it three weeks later
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(app.getAppointmentTime());
            calendar.add(Calendar.DAY_OF_YEAR, 21);

            bookAppointment(app.getUser(), app.getLocation(), calendar.getTime());
        }

        //delete the old appointment
        appointmentRepository.delete(app);

        response.sendRedirect("/");
    }

    @GetMapping("/viewAppts")
    public String viewApptsGet(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "viewAppointments";
    }


    @PostMapping(value = "/cancel/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void cancelApptPost(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable long id) throws IOException {
        Appointment app = appointmentRepository.getOne(id);
        appointmentRepository.delete(app);

        response.sendRedirect("/viewAppts");
    }
}
