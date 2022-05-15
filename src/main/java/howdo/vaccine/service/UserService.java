package howdo.vaccine.service;

import howdo.vaccine.enums.Nationality;
import howdo.vaccine.model.User;

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
                    Set<String> authorities);

    default User createUser(String ppsNumber,
                    String password,
                    String firstName,
                    String lastName,
                    Date dateOfBirth,
                    String phoneNumber,
                    String emailAddress,
                    Nationality nationality) {
        return this.createUser(ppsNumber, password, firstName, lastName, dateOfBirth, phoneNumber, emailAddress, nationality, Set.of("USER"));
    }

    void addLoginFailure(User user);
    void addLoginSuccess(User user);

    User getUser(String ppsNumber);

    User getCurrentUser();
}
