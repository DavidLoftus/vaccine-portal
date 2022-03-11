package howdo.vaccine.service;

import howdo.vaccine.model.User;
import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.UserActivityEvent;
import howdo.vaccine.model.VaccineDose;
import howdo.vaccine.repository.UserActivityEventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class ActivityTrackerServiceImpl implements ActivityTrackerService {

    @Autowired
    private UserActivityEventRepository eventRepository;

    @Override
    public void addUserEvent(User user, String eventDescription) {
        UserActivityEvent event = new UserActivityEvent();
        event.setUser(user);
        event.setDate(new Date());
        event.setDescription(eventDescription);

        eventRepository.save(event);
    }

    @Override
    public void userRegistered(User user) {
        addUserEvent(user, "Registered");
    }

    @Override
    public void userLogin(User user) {
        addUserEvent(user, "Login");
    }

    @Override
    public void userLogout(User user) {
        addUserEvent(user, "Logout");
    }

    @Override
    public void userBookAppointment(User user, Appointment appointment) {
        int numDoses = appointment.getUser().getDoses().size();
        addUserEvent(user, "Appointment for shot #" + (numDoses + 1) + " booked for " + appointment.getAppointmentTime() + " at " + appointment.getLocation().getName());
    }

    @Override
    public void userReceivedVaccine(User user, VaccineDose dose) {
        addUserEvent(user, "Received vaccine dose #" + dose.getDose());
    }

    @Override
    public void userCancelledAppointment(User user, Appointment appointment) {

    }

    @Override
    public List<UserActivityEvent> getUserEvents(User user) {
        return eventRepository.getByUser(user);
    }
}
