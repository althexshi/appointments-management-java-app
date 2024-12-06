package obj;

// Appointment Interface (+ String Helper Method)

import java.time.LocalDate;

public interface AppointmentHelper {
    default String toString(String type, String description, LocalDate startDate, LocalDate endDate) {
        return String.format("%s Appointment Details -- Description: %s, Start Date: %s, End Date: %s",
                type, description, startDate, endDate);
    }
}
