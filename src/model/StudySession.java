package model;

import java.time.LocalDate;

public class StudySession {
    private final LocalDate date;
    private final Topic topic;
    private final double hours;

    public StudySession(LocalDate date, Topic topic, double hours) {
        this.date = date;
        this.topic = topic;
        this.hours = hours;
    }

    public LocalDate getDate() { return date; }
    public Topic getTopic() { return topic; }
    public double getHours() { return hours; }

    @Override
    public String toString() {
        return String.format("%s | %-20s | %.1f hour(s)", date, topic.getName(), hours);
    }
}
