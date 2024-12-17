//AppointmentTest Class

package obj;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

class AppointmentTest {
    private Appointment appointment;
    private OnetimeAppointment onetimeAppointment;
    private MonthlyAppointment monthlyAppointment;

    private Appointment createAppointment(LocalDate startDate, LocalDate endDate, String description) {
        return new Appointment(startDate, endDate, description) {
            @Override
            public boolean occursOn(LocalDate date) {
                return false;
            }
        };
    }

    @BeforeEach
    void InitialDate() {
        LocalDate startDate = LocalDate.of(2024, 10, 14);
        LocalDate endDate = LocalDate.of(2024, 10, 16);
        String description = "";
        appointment = new Appointment(startDate, endDate, description) {
            @Override
            public boolean occursOn(LocalDate date) {
                return false;
            }
        };

        LocalDate oneTimeDate = LocalDate.of(2024, 10, 16);
        onetimeAppointment = new OnetimeAppointment("Meeting", oneTimeDate);

        LocalDate monthlyStartDate = LocalDate.of(2024, 10, 1);
        LocalDate monthlyEndDate = LocalDate.of(2024, 11, 30);
        monthlyAppointment = new MonthlyAppointment("Monthly Report", monthlyStartDate, monthlyEndDate);
    }

    @Test
    public void beforeStartDate() {
        LocalDate testDate = LocalDate.of(2024, 10, 1);
        assertFalse(appointment.inBetween(testDate));
    }

    @Test
    public void afterStartDate() {
        LocalDate testDate = LocalDate.of(2024, 10, 18);
        assertFalse(appointment.inBetween(testDate));
    }

    @Test
    public void betweenDates() {
        LocalDate testDate = LocalDate.of(2024, 10, 15);
        assertTrue(appointment.inBetween(testDate));
    }

    @Test
    public void onStartDate() {
        LocalDate testDate = LocalDate.of(2024, 10, 14);
        assertTrue(appointment.inBetween(testDate));
    }

    @Test
    public void onEndDate() {
        LocalDate testDate = LocalDate.of(2024, 10, 16);
        assertTrue(appointment.inBetween(testDate));
    }

    @Test
    public void OneTimeAppointmentCon() {
        assertEquals(onetimeAppointment.getStartDate(), onetimeAppointment.getEndDate());
    }

    @Test
    public void OneTimeAppointmentOccursOn() {
        assertTrue(onetimeAppointment.occursOn(LocalDate.of(2024, 10, 16)));
        assertFalse(onetimeAppointment.occursOn(LocalDate.of(2024, 10, 17)));
    }

    @Test
    public void MonthlyAppointmentOccursOn() {
        assertTrue(monthlyAppointment.occursOn(LocalDate.of(2024, 10, 16)));
        assertTrue(monthlyAppointment.occursOn(LocalDate.of(2024, 11, 16)));
        assertFalse(monthlyAppointment.occursOn(LocalDate.of(2024, 12, 1)));
    }

    @Test
    public void testEqualsMethod() {
        Appointment appointment2 = new Appointment(LocalDate.of(2024, 10, 14), LocalDate.of(2024, 10, 16), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return false;
            }
        };

        assertEquals(appointment, appointment2);

        Appointment differentAppointment = new Appointment(LocalDate.of(2024, 10, 15), LocalDate.of(2024, 10, 17), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return false;
            }
        };
        assertNotEquals(appointment, differentAppointment);
    }


    @Test
    public void CompareAppointments() {
        Appointment appointment1 = new Appointment(LocalDate.of(2024, 11, 22), LocalDate.of(2024, 11, 22), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        Appointment appointment2 = new Appointment(LocalDate.of(2024, 11, 20), LocalDate.of(2024, 11, 21), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        Appointment appointment3 = new Appointment(LocalDate.of(2024, 11, 21), LocalDate.of(2024, 11, 22), "") {
            @Override
            public boolean occursOn(LocalDate date) {
                return inBetween(date);
            }
        };

        //Puts the appointments in an array and sorts them
        Appointment[] appointments = {appointment1, appointment2, appointment3};
        Arrays.sort(appointments);

        //Test and checks the order
        assertEquals(appointment2, appointments[0], "This appointment will be first");
        assertEquals(appointment3, appointments[1], "This appointment will be second");
        assertEquals(appointment1, appointments[2], "This appointment will be third");
    }
}