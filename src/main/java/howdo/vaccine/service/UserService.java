package howdo.vaccine.service;

import howdo.vaccine.enums.Nationality;
import howdo.vaccine.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;

public interface UserService {
    User createUser(String ppsNumber,
                    String password,
                    String firstName,
                    String lastName,
                    Date dateOfBirth,
                    String phoneNumber,
                    String emailAddress,
                    Nationality nationality,
                    boolean uses2FA,
                    Set<String> authorities);

    default User createUser(String ppsNumber,
                    String password,
                    String firstName,
                    String lastName,
                    Date dateOfBirth,
                    String phoneNumber,
                    String emailAddress,
                    Nationality nationality,
                    boolean uses2FA) {
        return this.createUser(ppsNumber, password, firstName, lastName, dateOfBirth, phoneNumber, emailAddress, nationality, uses2FA, Set.of("USER"));
    }

    void addLoginFailure(User user);
    void addLoginSuccess(User user);

    User getUser(String ppsNumber) throws UsernameNotFoundException;

    User getCurrentUser();

    String generateQRUrl(User user);
}
