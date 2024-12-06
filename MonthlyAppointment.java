package obj;

// Monthly Appointment Class

import java.time.LocalDate;

public class MonthlyAppointment extends Appointment {
    private String description;

    public MonthlyAppointment(String description, LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        this.description = description;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return inBetween(date);
    }

    @Override
    public String toString() {
        return String.format("Monthly - %s from %s to %s", description, getStartDate(), getEndDate());
    }
}