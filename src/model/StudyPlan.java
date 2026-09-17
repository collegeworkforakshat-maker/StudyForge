package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudyPlan {
    private final List<StudySession> sessions = new ArrayList<>();

    public void addSession(StudySession session) {
        sessions.add(session);
    }

    public List<StudySession> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    public void printCalendar() {
        if (sessions.isEmpty()) {
            System.out.println("No study sessions could be generated.");
            return;
        }

        LocalDate current = null;
        for (StudySession session : sessions) {
            if (!session.getDate().equals(current)) {
                current = session.getDate();
                System.out.println("\n" + current + ":");
            }
            System.out.println("  -> " + session.getTopic().getName()
                    + " (" + String.format("%.1f", session.getHours()) + "h)");
        }
    }
}
