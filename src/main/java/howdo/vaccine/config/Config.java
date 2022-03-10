package howdo.vaccine.config;

import howdo.vaccine.service.ActivityTrackerService;
import howdo.vaccine.service.ActivityTrackerServiceImpl;
import howdo.vaccine.service.UserService;
import howdo.vaccine.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public ActivityTrackerService activityTrackerService() {
        return new ActivityTrackerServiceImpl();
    }
}
