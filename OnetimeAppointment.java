package obj;
// One Time Appointment Class

import java.time.LocalDate;

public class OnetimeAppointment extends Appointment {
    private String description;

    public OnetimeAppointment(String description, LocalDate startDate) {
        super(startDate, startDate); // One-time appointments have the same start and end dates
        this.description = description;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return inBetween(date);
    }

    @Override
    public String toString() {
        return String.format("One-time - %s from %s to %s", description, getStartDate(), getEndDate());
    }

}