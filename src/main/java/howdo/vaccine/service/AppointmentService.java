package howdo.vaccine.service;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;

import java.util.Date;

public interface AppointmentService {

    Appointment getAppointment(long id);

    Appointment bookNewAppointment(User user, Date date, VaccinationCentre centre);

    void cancelAppointment(Appointment appointment);

    void confirmAppointment(Appointment appointment, String vaccineType);

}
