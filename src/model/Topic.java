package model;

import java.time.LocalDate;

public class Topic {
    private final String id;
    private final String name;
    private final int difficulty;
    private final double estimatedHours;
    private LocalDate deadline;
    private Status status;

    public Topic(String id, String name, int difficulty, double estimatedHours, LocalDate deadline) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Topic ID cannot be empty.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Topic name cannot be empty.");
        if (difficulty < 1 || difficulty > 5) throw new IllegalArgumentException("Difficulty must be 1-5.");
        if (estimatedHours <= 0) throw new IllegalArgumentException("Estimated hours must be positive.");
        if (deadline == null) throw new IllegalArgumentException("Deadline cannot be null.");

        this.id = id.trim();
        this.name = name.trim();
        this.difficulty = difficulty;
        this.estimatedHours = estimatedHours;
        this.deadline = deadline;
        this.status = Status.NOT_STARTED;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getDifficulty() { return difficulty; }
    public double getEstimatedHours() { return estimatedHours; }
    public LocalDate getDeadline() { return deadline; }
    public Status getStatus() { return status; }

    public void setDeadline(LocalDate deadline) {
        if (deadline == null) throw new IllegalArgumentException("Deadline cannot be null.");
        this.deadline = deadline;
    }

    public void setStatus(Status status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Difficulty: %d/5 | %.1fh | Due: %s | %s",
                id, name, difficulty, estimatedHours, deadline, status);
    }
}
