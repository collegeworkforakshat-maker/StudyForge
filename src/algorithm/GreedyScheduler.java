package algorithm;

import graph.SyllabusGraph;
import model.*;

import java.time.LocalDate;
import java.util.*;

public class GreedyScheduler implements Scheduler {

    @Override
    public StudyPlan generate(SyllabusGraph graph, LocalDate startDate, double dailyHours) {
        if (dailyHours <= 0) throw new IllegalArgumentException("Daily hours must be positive.");

        List<Topic> remaining = new ArrayList<>();
        for (Topic topic : graph.getTopics()) {
            if (topic.getStatus() != Status.COMPLETED && topic.getStatus() != Status.SKIPPED) {
                remaining.add(topic);
            }
        }

        Map<String, Boolean> completed = new HashMap<>();
        for (Topic topic : graph.getTopics()) {
            completed.put(topic.getId(),
                    topic.getStatus() == Status.COMPLETED || topic.getStatus() == Status.SKIPPED);
        }

        StudyPlan plan = new StudyPlan();
        LocalDate date = startDate;
        double capacity = dailyHours;

        while (!remaining.isEmpty()) {
            List<Topic> available = new ArrayList<>();
            for (Topic topic : remaining) {
                boolean ready = true;
                for (Topic prerequisite : graph.getPrerequisites(topic.getId())) {
                    if (!completed.get(prerequisite.getId())) {
                        ready = false;
                        break;
                    }
                }
                if (ready) available.add(topic);
            }

            if (available.isEmpty()) {
                throw new IllegalStateException("No dependency-valid topic is available.");
            }

            available.sort(Comparator
                    .comparing(Topic::getDeadline)
                    .thenComparing(Comparator.comparingInt(Topic::getDifficulty).reversed())
                    .thenComparing(Topic::getName));

            Topic selected = available.get(0);
            double sessionHours = Math.min(selected.getEstimatedHours(), capacity);

            plan.addSession(new StudySession(date, selected, sessionHours));
            selected = findById(remaining, selected.getId());

            double left = selected.getEstimatedHours() - sessionHours;
            if (left <= 0.0001) {
                completed.put(selected.getId(), true);
                remaining.remove(selected);
            }

            // For a multi-day topic, update its remaining workload by replacing it.
            if (left > 0.0001) {
                Topic partial = new Topic(selected.getId(), selected.getName(),
                        selected.getDifficulty(), left, selected.getDeadline());
                partial.setStatus(selected.getStatus());
                int index = remaining.indexOf(selected);
                remaining.set(index, partial);
            }

            capacity -= sessionHours;
            if (capacity <= 0.0001) {
                date = date.plusDays(1);
                capacity = dailyHours;
            }
        }
        return plan;
    }

    private Topic findById(List<Topic> topics, String id) {
        for (Topic t : topics) if (t.getId().equals(id)) return t;
        throw new IllegalStateException("Internal scheduling error.");
    }

    @Override
    public String getName() {
        return "Greedy Deadline-First Scheduler";
    }
}
