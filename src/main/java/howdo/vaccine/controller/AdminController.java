package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.repository.VaccinationCentreRepository;
import howdo.vaccine.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private VaccinationCentreRepository centreRepository;

    @ModelAttribute
    public String getPage() {
        return "admin";
    }

    @ModelAttribute("locations")
    public List<VaccinationCentre> getLocations() {
        return centreRepository.findAll();
    }

    @Secured({"ADMIN"})
    @GetMapping("/admin")
    public String editGet(Model model) {
        model.addAttribute("page", "admin");
        return "editVaccineAppt";
    }

    @Secured({"ADMIN"})
    @PostMapping(value = "/admin/edit/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void editPost(HttpServletResponse response, HttpServletRequest request,
                         @PathVariable long id) throws IOException {
        Appointment appointment = appointmentService.getAppointment(id);

        appointmentService.confirmAppointment(appointment, request.getParameter("type"));

        response.sendRedirect("/admin");
    }

}
