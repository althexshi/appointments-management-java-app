package obj;

// Monthly Appointment Class

import java.time.LocalDate;

public class MonthlyAppointment extends Appointment {


    public MonthlyAppointment(String description, LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate, description);

    }

    @Override
    public boolean occursOn(LocalDate date) {
        return inBetween(date);
    }

    @Override
    public String toString() {
        return "Monthly - " + super.toString();
    }
}