package howdo.vaccine.service;

import howdo.vaccine.enums.Nationality;
import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {

    private final int MAX_LOGIN_FAILURES = 3;
    private final Duration ACCOUNT_LOCK_TIME = Duration.ofMinutes(20);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ActivityTrackerService activityTrackerService;

    @Override
    public User createUser(String ppsNumber, String password, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String emailAddress, Nationality nationality, Set<String> authorities) {
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

        user = userRepository.save(user);

        activityTrackerService.userRegistered(user);

        return user;
    }

    @Override
    public void addLoginFailure(User user) {
        int attempts = user.getLoginAttempts() + 1;
        if (attempts >= MAX_LOGIN_FAILURES) {
            user.setLoginAttempts(0);
            Instant instant = new Date().toInstant().plus(Duration.ofMinutes(20));
            user.setAccountLockExpiry(Date.from(instant));
        } else {
            user.setLoginAttempts(attempts);
        }
        userRepository.save(user);
    }

    @Override
    public void addLoginSuccess(User user) {
        user.setLoginAttempts(0);
        userRepository.save(user);
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
}
