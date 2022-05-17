package howdo.vaccine.service;

import howdo.vaccine.enums.Nationality;
import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ActivityTrackerService activityTrackerService;

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    @Override
    public User createUser(String ppsNumber, String password, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String emailAddress, Nationality nationality, boolean uses2FA, Set<String> authorities) {
        User user = new User();
        user.setPpsNumber(ppsNumber);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setPhoneNumber(phoneNumber);
        user.setEmailAddress(emailAddress);
        user.setNationality(nationality);
        user.setAuthorities(authorities);
        user.setUsing2FA(uses2FA);

        user = userRepository.save(user);

        activityTrackerService.userRegistered(user);

        return user;
    }

    @Override
    public User getUser(String ppsNumber) {
        List<User> users = userRepository.getUserByPpsNumber(ppsNumber);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("No such user with pps " + ppsNumber);
        }
        return users.get(0);
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (username.equals("anonymousUser")) {
            return null;
        }

        return getUser(username);
    }

    @Override
    public String generateQRUrl(User user) {
        return QR_PREFIX + URLEncoder.encode(String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                "vaxapp", user.getEmailAddress(), user.getSecret(), "vaxapp"),
                StandardCharsets.UTF_8);
    }
}
