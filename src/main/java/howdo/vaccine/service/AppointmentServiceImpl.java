package howdo.vaccine.service;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import howdo.vaccine.model.VaccinationCentre;
import howdo.vaccine.model.VaccineDose;
import howdo.vaccine.repository.VaccineAptRepository;
import howdo.vaccine.repository.VaccineDoseRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public Appointment bookNewAppointment(User user, LocalDateTime date, VaccinationCentre centre) throws BookingUnavailable {
        if (user.getDoses().size() >= 2) {
            throw new BookingUnavailable("You are already fully vaccinated");
        }

        if (!user.getAppointments().isEmpty()) {
            throw new BookingUnavailable("You already have an appointment booked");
        }

        if (!isTimeValid(date)) {
            throw new BookingUnavailable("Invalid time " + date);
        }

        if (isSlotTaken(date, centre)) {
            throw new BookingUnavailable("Time slot taken");
        }


        Appointment appointment = new Appointment();

        appointment.setUser(user);
        appointment.setLocation(centre);
        appointment.setAppointmentTime(date);
        appointment = appointmentRepository.save(appointment);

        activityTrackerService.userBookAppointment(user, appointment);

        return appointment;
    }

    private boolean isTimeValid(LocalDateTime date) {
        return date.toLocalTime().getMinute() % 15 == 0 && LocalDateTime.now().isBefore(date);
    }

    @Override
    public Appointment autobookNewAppointment(User user, LocalDate date, VaccinationCentre centre) throws BookingUnavailable {
        List<LocalTime> times = getAvailableTimes(centre, date);
        while (times.isEmpty()) {
            date = date.plusDays(1);
            times = getAvailableTimes(centre, date);
        }

        return bookNewAppointment(user, date.atTime(times.get(0)), centre);
    }

    private Date localDateTimeToDateTime(LocalDateTime date) {
        return Date.from(Instant.from(date.atZone(ZoneId.systemDefault())));
    }

    @Override
    public List<LocalTime> getAvailableTimes(VaccinationCentre centre, LocalDate day) {
        List<Appointment> bookedAppointments = appointmentRepository.findAppointmentsOnDay(
                centre,
                day.atStartOfDay(),
                day.plusDays(1).atStartOfDay()
        );

        Set<LocalTime> booked = bookedAppointments
                .stream()
                .map(Appointment::getAppointmentTime)
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toSet());

        List<LocalTime> available = new ArrayList<>();

        final LocalTime openingTime = LocalTime.of(9, 0);
        final LocalTime closingTime = LocalTime.of(18, 0);

        for (LocalTime t = LocalTime.from(openingTime); t.isBefore(closingTime); t = t.plusMinutes(15)) {
            if (!booked.contains(t)) {
                available.add(t);
            }
        }

        return available;
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
        dose.setDate(localDateTimeToDateTime(appointment.getAppointmentTime()));
        dose.setVaccineType(vaccineType);
        dose = doseRepository.save(dose);

        activityTrackerService.userReceivedVaccine(user, dose);

        //delete the old appointment
        appointmentRepository.delete(appointment);

        //book a second appointment if this is the first one
        if (dose.getDose() == 1)
        {
            LocalDate threeWeeksLater = appointment.getAppointmentTime().toLocalDate().plusDays(21);

            try {
                autobookNewAppointment(user, threeWeeksLater, appointment.getLocation());
            } catch (BookingUnavailable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isSlotTaken(LocalDateTime date, VaccinationCentre location) {
        return !getAvailableTimes(location, date.toLocalDate()).contains(date.toLocalTime());
    }
}
