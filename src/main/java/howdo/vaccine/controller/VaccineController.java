package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.repository.VaccineCentreRepository;
import howdo.vaccine.service.ActivityTrackerService;
import howdo.vaccine.service.AppointmentService;
import howdo.vaccine.service.BookingUnavailable;
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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class VaccineController {
    @Autowired
    VaccineCentreRepository centreRepository;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    UserService userService;

    @Autowired
    private ActivityTrackerService activityTrackerService;

    @ModelAttribute("page")
    public String getPage() {
        return "appointments";
    }


    //add a list of VaccinationCentres to the model
    @ModelAttribute
    public void addAttributes(Model model) {
        List<VaccinationCentre> locations = centreRepository.findAll();
        model.addAttribute("locations", locations);
    }

    @PostMapping(value = "/appointments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String newAppointmentPost(Model model, HttpServletResponse response, HttpServletRequest request, @ModelAttribute Appointment appointment) throws IOException, ParseException {
        //parse the data from the form
        User user = userService.getCurrentUser();
        String locationString = request.getParameter("location");   //contains the ID of the VaccinationCentre
        VaccinationCentre location = centreRepository.getOne(Long.parseLong(locationString));

        try {
            appointmentService.bookNewAppointment(user, appointment.getAppointmentTime(), location);
        } catch (BookingUnavailable e) {
            model.addAttribute("error", e);
        }
        return viewApptsGet(model);
    }

    @GetMapping("/edit")
    public String editGet(Model model) {
        model.addAttribute("page", "admin");
        return "editVaccineAppt";
    }


    @PostMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void editPost(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable long id) throws IOException, ParseException {
        Appointment appointment = appointmentService.getAppointment(id);

        appointmentService.confirmAppointment(appointment, request.getParameter("type"));

        response.sendRedirect("/");
    }

    @GetMapping("/appointments")
    public String viewApptsGet(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("appointment", new Appointment());
        return "viewAppointments";
    }


    @PostMapping(value = "/cancel/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void cancelApptPost(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable long id) throws IOException {
        Appointment appointment = appointmentService.getAppointment(id);
        appointmentService.cancelAppointment(appointment);

        response.sendRedirect("/appointments");
    }
}
