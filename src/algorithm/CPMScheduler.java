package algorithm;

import graph.SyllabusGraph;
import model.*;

import java.time.LocalDate;
import java.util.*;

public class CPMScheduler implements Scheduler {

    @Override
    public StudyPlan generate(SyllabusGraph graph, LocalDate startDate, double dailyHours) {
        if (dailyHours <= 0) throw new IllegalArgumentException("Daily hours must be positive.");

        List<Topic> remaining = new ArrayList<>();
        for (Topic topic : graph.getTopics()) {
            if (topic.getStatus() != Status.COMPLETED && topic.getStatus() != Status.SKIPPED) {
                remaining.add(topic);
            }
        }

        Map<String, Double> weights = new HashMap<>();
        for (Topic topic : remaining) {
            weights.put(topic.getId(), criticalWeight(topic, graph, new HashSet<>()));
        }

        Map<String, Boolean> done = new HashMap<>();
        for (Topic topic : graph.getTopics()) {
            done.put(topic.getId(),
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
                    if (!done.get(prerequisite.getId())) {
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
                    .comparingDouble((Topic t) -> weights.get(t.getId())).reversed()
                    .thenComparing(Topic::getDeadline)
                    .thenComparing(Topic::getName));

            Topic selected = available.get(0);
            double sessionHours = Math.min(selected.getEstimatedHours(), capacity);
            plan.addSession(new StudySession(date, selected, sessionHours));

            double left = selected.getEstimatedHours() - sessionHours;
            if (left <= 0.0001) {
                done.put(selected.getId(), true);
                remaining.remove(selected);
            } else {
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

    private double criticalWeight(Topic topic, SyllabusGraph graph, Set<String> path) {
        if (!path.add(topic.getId())) return 0;
        double bestChild = 0;

        Set<String> children = graph.getAdjacency().get(topic.getId());
        if (children != null) {
            for (String childId : children) {
                for (Topic child : graph.getTopics()) {
                    if (child.getId().equals(childId)) {
                        bestChild = Math.max(bestChild,
                                criticalWeight(child, graph, new HashSet<>(path)));
                    }
                }
            }
        }
        return topic.getEstimatedHours() + bestChild;
    }

    @Override
    public String getName() {
        return "Critical-Path-Weighted Scheduler";
    }
}
