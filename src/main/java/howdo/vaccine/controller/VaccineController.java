package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.repository.VaccineCentreRepository;
import howdo.vaccine.service.ActivityTrackerService;
import howdo.vaccine.service.AppointmentService;
import howdo.vaccine.service.BookingUnavailable;
import howdo.vaccine.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class VaccineController {

    private static final Logger logger = LogManager.getLogger(VaccineController.class);


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

    @GetMapping("/appointments/times")
    public String getAvailableTimes(Model model,
                                    @RequestParam("location") long locationId,
                                    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        VaccinationCentre vaccinationCentre = centreRepository.getOne(locationId);
        model.addAttribute("times", appointmentService.getAvailableTimes(vaccinationCentre, date));

        return "times";
    }

    @PostMapping(value = "/appointments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String newAppointmentPost(HttpServletResponse response, Model model,
                                     @RequestParam("location") long locationId,
                                     @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) throws IOException {

        //parse the data from the form
        User user = userService.getCurrentUser();
        VaccinationCentre location = centreRepository.getOne(locationId);

        try {
            Appointment appointment = appointmentService.bookNewAppointment(user, date.atTime(time), location);
            logger.info("User \"" + userService.getCurrentUser().getId() + "\" has booked a new appointment: " + appointment.getId() + "\"");
            response.sendRedirect("/appointments");
        } catch (BookingUnavailable e) {
            model.addAttribute("error", e);
        }



        return viewApptsGet(model);
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
        User user = userService.getCurrentUser();
        Appointment appointment = appointmentService.getAppointment(id);

        if (appointment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Appointment does not belong to current user.");
        }

        appointmentService.cancelAppointment(appointment);

        logger.info("User \"" + userService.getCurrentUser().getId() + "\" has cancelled their appointment: " + appointment.getId());

        response.sendRedirect("/appointments");
    }
}
