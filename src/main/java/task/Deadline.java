package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the Deadline task
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by, boolean done) {
        super(description, done);
        this.by = by;
    }

    /**
     * Returns the deadline of the task.
     *
     * @return Date and time of the deadline.
     */
    public LocalDateTime getTiming() {
        return this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(DateTimeFormatter.ofPattern("MMM d yyyy, HH:mm")) + ")";
    }
}