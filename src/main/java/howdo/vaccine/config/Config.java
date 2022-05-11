package howdo.vaccine.config;

import howdo.vaccine.service.*;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
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

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    @Value("${http.port}")
    private int httpPort;

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        return connector;
    }


}
