package howdo.vaccine.service;

import howdo.vaccine.model.User;
import howdo.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(String ppsNumber, String password, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String emailAddress, String nationality, Set<String> authorities) {
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

        return userRepository.save(user);
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

        return getUser(username);
    }
}