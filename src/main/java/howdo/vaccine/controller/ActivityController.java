package howdo.vaccine.controller;

import howdo.vaccine.model.UserActivityEvent;
import howdo.vaccine.service.ActivityTrackerService;
import howdo.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    private ActivityTrackerService activityTrackerService;

    @Autowired
    private UserService userService;

    @ModelAttribute("page")
    public String getPage() {
        return "activity";
    }

    @ModelAttribute("events")
    public List<UserActivityEvent> getEvents() {
        return activityTrackerService.getUserEvents(userService.getCurrentUser());
    }

    @GetMapping("/activity")
    public String getActivity() {
        return "activity";
    }

}
