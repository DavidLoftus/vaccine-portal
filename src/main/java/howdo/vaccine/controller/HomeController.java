package howdo.vaccine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller("/")
public class HomeController {

    @ModelAttribute("page")
    public String getPage() {
        return "home";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
