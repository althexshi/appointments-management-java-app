package obj;
// One Time Appointment Class

import java.time.LocalDate;

public class OnetimeAppointment extends Appointment {

    public OnetimeAppointment(String description, LocalDate startDate) {
        super(startDate, startDate, description); // One-time appointments have the same start and end dates

    }

    @Override
    public boolean occursOn(LocalDate date) {
        return inBetween(date);
    }

    @Override
    public String toString() {
        return "One-time - " + super.toString();
    }

}