package howdo.vaccine.config;

import howdo.vaccine.service.*;
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

    @Bean
    public AppointmentService appointmentService() {
        return new AppointmentServiceImpl();
    }
}
