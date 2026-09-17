package service;

import graph.SyllabusGraph;
import model.Status;
import model.Topic;

public class ProgressTracker {
    private final SyllabusGraph graph;

    public ProgressTracker(SyllabusGraph graph) {
        this.graph = graph;
    }

    public void updateStatus(String topicId, Status status) {
        for (Topic topic : graph.getTopics()) {
            if (topic.getId().equals(topicId)) {
                topic.setStatus(status);
                return;
            }
        }
        throw new IllegalArgumentException("Topic not found: " + topicId);
    }

    public void printProgress() {
        int total = graph.getTopics().size();
        if (total == 0) {
            System.out.println("No topics available.");
            return;
        }

        int completed = 0;
        for (Topic topic : graph.getTopics()) {
            if (topic.getStatus() == Status.COMPLETED) completed++;
        }

        double percentage = completed * 100.0 / total;
        System.out.printf("Progress: %d/%d topics completed (%.1f%%)%n",
                completed, total, percentage);

        for (Topic topic : graph.getTopics()) {
            System.out.println("  " + topic.getId() + " - " + topic.getName()
                    + " [" + topic.getStatus() + "]");
        }
    }
}
