package howdo.vaccine.service;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    Appointment getAppointment(long id);

    Appointment bookNewAppointment(User user, LocalDateTime dateTime, VaccinationCentre centre) throws BookingUnavailable;
    Appointment autobookNewAppointment(User user, LocalDate dateTime, VaccinationCentre centre) throws BookingUnavailable;


    List<LocalTime> getAvailableTimes(VaccinationCentre centre, LocalDate day);

    void cancelAppointment(Appointment appointment);

    void confirmAppointment(Appointment appointment, String vaccineType);

    boolean isSlotTaken(LocalDateTime date, VaccinationCentre location);
}
