package obj;

//AppointmentManager Class

import java.time.LocalDate;
import java.util.*;

public class AppointmentManager {
    private HashSet<Appointment> appointments;

    public AppointmentManager() {
        this.appointments = new HashSet<>();
    }

    public HashSet<Appointment> getAppointments() {
        return appointments;
    }

    public void add(Appointment appointment) {
        if (!appointments.add(appointment)) {
            throw new IllegalArgumentException("Appointment exists!");
        }
    }

    public void delete(Appointment appointment) {
        if (!appointments.remove(appointment)) {
            throw new IllegalArgumentException("Appointment not found!");
        }
    }

    public void update(Appointment current, Appointment modified) {
        delete(current);
        add(modified);
    }

    public Appointment[] getAppointmentsOn(LocalDate date, Comparator<Appointment> order) {
        if (date == null && order == null) {
            return appointments.toArray(new Appointment[0]);
        }

        PriorityQueue<Appointment> queue;
        if (order != null) queue = new PriorityQueue<>(order);
        else queue = new PriorityQueue<>(Comparator.naturalOrder());

        for (Appointment appointment : appointments) {
            if (date == null || appointment.occursOn(date)) {
                queue.add(appointment);
            }
        }

        return queue.toArray(new Appointment[0]);
    }
}