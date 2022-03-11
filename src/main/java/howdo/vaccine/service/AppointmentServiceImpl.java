package howdo.vaccine.service;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.model.VaccineDose;
import howdo.vaccine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private VaccineAptRepository appointmentRepository;

    @Autowired
    private VaccineDoseRepository doseRepository;

    @Autowired
    private ActivityTrackerService activityTrackerService;

    @Override
    public Appointment getAppointment(long id) {
        return appointmentRepository.getOne(id);
    }

    @Override
    public Appointment bookNewAppointment(User user, Date date, VaccinationCentre centre) {
        Appointment appointment = new Appointment();

        appointment.setUser(user);
        appointment.setLocation(centre);
        appointment.setAppointmentTime(date);
        appointment = appointmentRepository.save(appointment);

        activityTrackerService.userBookAppointment(user, appointment);

        return appointment;
    }

    @Override
    public void cancelAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
        activityTrackerService.userCancelledAppointment(appointment.getUser(), appointment);
    }

    @Override
    public void confirmAppointment(Appointment appointment, String vaccineType) {
        VaccineDose dose = new VaccineDose();

        User user = appointment.getUser();

        dose.setUser(user);
        dose.setDose(user.getDoses().isEmpty() ? 1 : 2);
        dose.setDate(appointment.getAppointmentTime());
        dose.setVaccineType(vaccineType);
        dose = doseRepository.save(dose);

        activityTrackerService.userReceivedVaccine(user, dose);

        //book a second appointment if this is the first one
        if (dose.getDose() == 1)
        {
            //book it three weeks later
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(appointment.getAppointmentTime());
            calendar.add(Calendar.DAY_OF_YEAR, 21);

            bookNewAppointment(user, calendar.getTime(), appointment.getLocation());
        }

        //delete the old appointment
        appointmentRepository.delete(appointment);
    }
}
