package obj;

// Appointment Class

import java.time.LocalDate;
import java.util.Objects;

public abstract class Appointment implements Comparable<Appointment> {
    private LocalDate startDate;
    private LocalDate endDate;

    public Appointment(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected boolean inBetween(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public abstract boolean occursOn(LocalDate date);

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Appointment)) return false;
        Appointment other = (Appointment) obj;
        return startDate.equals(other.startDate) && endDate.equals(other.endDate);
    }

    @Override
    public int compareTo(Appointment other) {
        int startComparison = this.startDate.compareTo(other.startDate);
        if (startComparison != 0) return startComparison;

        int endComparison = this.endDate.compareTo(other.endDate);
        if (endComparison != 0) return endComparison;

        return this.toString().compareTo(other.toString());
    }

}