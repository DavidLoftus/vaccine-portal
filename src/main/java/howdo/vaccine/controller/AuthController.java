package howdo.vaccine.controller;

import howdo.vaccine.config.IpFilterAuthenticationProvider;
import howdo.vaccine.enums.Nationality;
import howdo.vaccine.model.User;
import howdo.vaccine.model.UserRegistrationForm;
import howdo.vaccine.service.UserDetailsServiceImpl;
import howdo.vaccine.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Controller
public class AuthController {

    private static final Logger authLogger = LogManager.getLogger(AuthController.class);


    @Autowired
    UserService userService;

//    @Autowired
//    IpFilterAuthenticationProvider ipFilter;

    @GetMapping("/register")
    public String registerGet(@ModelAttribute("user") UserRegistrationForm user) {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerPost(HttpServletResponse response, @Valid @ModelAttribute("user") UserRegistrationForm form) throws IOException {

        User user = userService.createUser(form.getPpsNumber(),
                form.getPassword(),
                form.getFirstName(),
                form.getLastName(),
                form.getDateOfBirth(),
                form.getPhoneNumber(),
                form.getEmailAddress(),
                form.getNationality());

        AuthController.authLogger.info("New user \"" + user.getId() + "\" has been created");

        response.sendRedirect("/");
    }

    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @Value("${portal.admin.password}")
    private String adminPassword = "password";

    @PostConstruct
    public void init() {
        try {
            userService.getUser("admin");
        } catch (UsernameNotFoundException e) {
            userService.createUser("admin", adminPassword, "John", "Smith",
                    new Date(), "0123456789", "admin@localhost", Nationality.IRISH,
                    Set.of("USER", "ADMIN"));
        }
    }

    private final Map<String, Integer> authFailures = new HashMap<>();

    private void addStrikeToIp(String ip) {
        if (!authFailures.containsKey(ip)) {
            authFailures.put(ip, 1);
            return;
        }

        int count = authFailures.getOrDefault(ip, 0);
        count++;

        if (count >= 5) {
            count = 0;
            authLogger.warn("IP " + ip + " has exceeded maximum failed login attempts, banning for 20 minutes.");
            //ipFilter.banAddress(ip);
        }
        authFailures.put(ip, count);
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        try {
            Object principal = authentication.getPrincipal();
            User user;
            if (principal instanceof UserDetailsServiceImpl.MyUserDetails) {
                user = ((UserDetailsServiceImpl.MyUserDetails) principal).getUser();
            } else {
                user = userService.getUser((String) principal);
            }
            authLogger.warn("Failed attempt to login to user " + user.getId());
            userService.addLoginFailure(user);
        } catch (UsernameNotFoundException ignored) {}
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            addStrikeToIp(details.getRemoteAddress());
        }
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        UserDetailsServiceImpl.MyUserDetails userDetails = (UserDetailsServiceImpl.MyUserDetails) authentication.getPrincipal();
        userService.addLoginSuccess(userDetails.getUser());

        authLogger.info("User \"" + userDetails.getUser().getId() + "\" has logged in");
    }



    @EventListener
    public void logoutSuccess(LogoutSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        UserDetailsServiceImpl.MyUserDetails userDetails = (UserDetailsServiceImpl.MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        authLogger.info("User \"" + user.getId() + "\" has logged out");

    }

}
