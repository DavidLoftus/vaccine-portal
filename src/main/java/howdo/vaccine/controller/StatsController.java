package howdo.vaccine.controller;

import howdo.vaccine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VaccineDoseRepository vaccineDoseRepository;

    @Autowired
    VaccinationCentreRepository vaccinationCentreRepository;

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
        //ArrayList<Integer> ageList = userRepository.userAges();

        model.addAttribute("doseTotal", doseTotal);
        model.addAttribute("userTotal", userTotal);
        model.addAttribute("vaccinatedPercentage", vaccinatedPercentage);
        model.addAttribute("vaccinationCentreTotal", vaccinationCentreTotal);
        model.addAttribute("forumPostTotal", forumPostTotal);
        model.addAttribute("appointmentTotal", appointmentTotal);
        model.addAttribute("zeroDosesTotal", zeroDosesTotal);
        model.addAttribute("oneDosesTotal", oneDosesTotal);
        model.addAttribute("twoDosesTotal", twoDosesTotal);
        return "stats";
    }


}
