package algorithm;

import graph.SyllabusGraph;
import model.StudyPlan;

import java.time.LocalDate;

public interface Scheduler {
    StudyPlan generate(SyllabusGraph graph, LocalDate startDate, double dailyHours);
    String getName();
}
