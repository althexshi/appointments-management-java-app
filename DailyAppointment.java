package obj;

import java.time.LocalDate;

public class DailyAppointment extends Appointment {
    private String description;

    public DailyAppointment(String description, LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        this.description = description;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return inBetween(date);
    }

    @Override
    public String toString() {
        return String.format("Daily - %s from %s to %s", description, getStartDate(), getEndDate());
    }
}

