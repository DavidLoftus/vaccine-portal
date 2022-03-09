package howdo.vaccine.controller;

import howdo.vaccine.repository.UserRepository;
import howdo.vaccine.repository.VaccineDoseRepository;
import howdo.vaccine.repository.VaccinationCentreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class StatsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VaccineDoseRepository vaccineDoseRepository;

    @Autowired
    VaccinationCentreRepository vaccinationCentreRepository;

    @RequestMapping({"/stats"})
    public String viewHomePage(Model model) {
        int userTotal = userRepository.userTotal();
        int doseTotal = vaccineDoseRepository.doseTotal();
        int vaccinatedPercentage = (vaccineDoseRepository.doseTotal()/5000000)*100;
        int vaccinationCentreTotal = vaccinationCentreRepository.vaccinationCentreTotal();
        //ArrayList<Integer> ageList = userRepository.userAges();

        model.addAttribute("doseTotal", doseTotal);
        model.addAttribute("userTotal", userTotal);
        model.addAttribute("vaccinatedPercentage", vaccinatedPercentage);
        model.addAttribute("vaccinationCentreTotal", vaccinationCentreTotal);
        return "stats";
    }


}
