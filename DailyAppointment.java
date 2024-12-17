package obj;

import java.time.LocalDate;

public class DailyAppointment extends Appointment {


    public DailyAppointment(String description, LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate, description);

    }

    @Override
    public boolean occursOn(LocalDate date) {
        return inBetween(date);
    }

    @Override
    public String toString() {
        return "Daily - " + super.toString();
    }
}
