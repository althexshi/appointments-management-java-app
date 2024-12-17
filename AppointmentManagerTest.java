package obj;

//AppointmentManagerTest Class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentManagerTest {

    private AppointmentManager appointmentManager;

    @BeforeEach
    public void initialSetup() {
        appointmentManager = new AppointmentManager();
    }

    //testing add method
    @Test
    public void testAdd() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 11, 23), LocalDate.of(2024, 11, 24), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        assertEquals(0, appointmentManager.getAppointments().size()); //initializing
        appointmentManager.add(appointment);
        assertEquals(1, appointmentManager.getAppointments().size());
        assertTrue(appointmentManager.getAppointments().contains(appointment));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentManager.add(appointment);
        });
        assertEquals("Appointment exists!", exception.getMessage());
    }

    //testing delete method
    @Test
    public void testDelete() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 11, 23), LocalDate.of(2024, 11, 24), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };
        appointmentManager.add(appointment);
        assertEquals(1, appointmentManager.getAppointments().size());

        appointmentManager.delete(appointment);
        assertEquals(0, appointmentManager.getAppointments().size());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentManager.delete(appointment);
        });
        assertEquals("Appointment not found!", exception.getMessage());
    }

    //testing update method
    @Test
    public void testUpdate() {
        Appointment oldAppointment = new Appointment(LocalDate.of(2024, 11, 23), LocalDate.of(2024, 11, 24), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        Appointment newAppointment = new Appointment(LocalDate.of(2024, 11, 25), LocalDate.of(2024, 11, 26), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        appointmentManager.add(oldAppointment);
        assertEquals(1, appointmentManager.getAppointments().size());

        appointmentManager.update(oldAppointment, newAppointment);
        assertEquals(1, appointmentManager.getAppointments().size());
        assertTrue(appointmentManager.getAppointments().contains(newAppointment));
        assertFalse(appointmentManager.getAppointments().contains(oldAppointment));
    }

    // Test getAppointmentsOn method
    @Test
    public void testGetAppointmentsOn() {
        // Initialize appointmentManager
        AppointmentManager appointmentManager = new AppointmentManager();

        Appointment appointment1 = new Appointment(LocalDate.of(2024, 11, 23), LocalDate.of(2024, 11, 24), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        Appointment appointment2 = new Appointment(LocalDate.of(2024, 11, 23), LocalDate.of(2024, 11, 23), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        Appointment appointment3 = new Appointment(LocalDate.of(2024, 11, 24), LocalDate.of(2024, 11, 25), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        appointmentManager.add(appointment1);
        appointmentManager.add(appointment2);
        appointmentManager.add(appointment3);

        //given or nulled date
        Appointment[] appointmentsOnDate;
        appointmentsOnDate = appointmentManager.getAppointmentsOn(LocalDate.of(2024, 11, 23), null);
        assertNotNull(appointmentsOnDate);
        assertEquals(2, appointmentsOnDate.length);
        assertTrue(existingAppointment(appointmentsOnDate, appointment1));
        assertTrue(existingAppointment(appointmentsOnDate, appointment2));

        //date and appointment nulled
        Appointment[] allAppointments;
        allAppointments = appointmentManager.getAppointmentsOn(null, null);
        assertNotNull(allAppointments);
        assertEquals(3, allAppointments.length);

        //appoinment given
        Comparator<Appointment> order;
        order = Comparator.comparing(Appointment::getStartDate);
        Appointment[] sortedAppointments = appointmentManager.getAppointmentsOn(LocalDate.of(2024, 11, 23), order);
        assertNotNull(sortedAppointments);
        assertEquals(2, sortedAppointments.length); // Ensure we have 2 appointments
        assertFalse(sortedAppointments[0].getStartDate().isAfter(sortedAppointments[1].getStartDate()));
    }
    private boolean existingAppointment(Appointment[] appointments, Appointment appointment) {
        for (Appointment app : appointments) {
            if (appointment.equals(app)) {
                return true;
            }
        }
        return false;
    }
}