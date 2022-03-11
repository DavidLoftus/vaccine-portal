package howdo.vaccine.controller;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccineDose;
import howdo.vaccine.repository.*;
import howdo.vaccine.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StatsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VaccineDoseRepository vaccineDoseRepository;

    @Autowired
    VaccinationCentreRepository vaccinationCentreRepository;

    @Autowired
    private UserService userService;


    @ModelAttribute("page")
    public String getPage() {
        return "home";
    }

    @Autowired
    ForumPostRepository forumPostRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @RequestMapping({"/"})
    public String viewHomePage(Model model) {
        int userTotal = userRepository.userTotal();
        int doseTotal = vaccineDoseRepository.doseTotal();
        int vaccinatedPercentage = (vaccineDoseRepository.doseTotal()/5000000)*100;
        int vaccinationCentreTotal = vaccinationCentreRepository.vaccinationCentreTotal();
        int forumPostTotal = forumPostRepository.forumPostTotal();
        int appointmentTotal = appointmentRepository.appointmentTotal();
        int zeroDosesTotal = userRepository.zeroDosesTotal();
        int oneDosesTotal = userRepository.oneDosesTotal();
        int twoDosesTotal = userRepository.twoDosesTotal();

        model.addAttribute("doseTotal", doseTotal);
        model.addAttribute("userTotal", userTotal);
        model.addAttribute("vaccinatedPercentage", vaccinatedPercentage);
        model.addAttribute("vaccinationCentreTotal", vaccinationCentreTotal);
        model.addAttribute("forumPostTotal", forumPostTotal);
        model.addAttribute("appointmentTotal", appointmentTotal);
        model.addAttribute("zeroDosesTotal", zeroDosesTotal);
        model.addAttribute("oneDosesTotal", oneDosesTotal);
        model.addAttribute("twoDosesTotal", twoDosesTotal);


        User currentUser = userService.getCurrentUser();
        Long ID = currentUser.getId();
        String firstName = userRepository.getUserFirstName(ID);
        String lastName = userRepository.getUserLastName(ID);
        String fullName = firstName + " " + lastName;
        String PPS = userRepository.getPpsNumber(ID);
        String birthDate = userRepository.getDateOfBirth(ID);

        String phoneNumber = userRepository.getPhoneNumber(ID);
        String email = userRepository.getEmailAddress(ID);
        String nationality = userRepository.getNationality(ID);

        List<Appointment> appointments = currentUser.getAppointments();
        List<VaccineDose> userDoses = currentUser.getDoses();

        model.addAttribute("fullName", fullName);
        model.addAttribute("PPS", PPS);
        model.addAttribute("birthDate", birthDate);

        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("email", email);
        model.addAttribute("nationality", nationality);
        model.addAttribute("appointments", appointments);
        model.addAttribute("userDoses", userDoses);


        return "stats";
    }


}
