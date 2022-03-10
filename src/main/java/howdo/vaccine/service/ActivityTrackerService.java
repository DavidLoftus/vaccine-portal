package howdo.vaccine.service;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.UserActivityEvent;
import howdo.vaccine.model.VaccineDose;

import java.util.List;

public interface ActivityTrackerService {

    void addUserEvent(User user, String eventDescription);

    void userRegistered(User user);
    void userLogin(User user);
    void userLogout(User user);
    void userBookAppointment(User user, Appointment appointment);
    void userReceivedVaccine(User user, VaccineDose dose);

    List<UserActivityEvent> getUserEvents(User user);
}
