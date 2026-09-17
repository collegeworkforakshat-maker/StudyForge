import algorithm.GreedyScheduler;
import graph.SyllabusGraph;
import model.*;

import java.time.LocalDate;

public class StudyForgeTest {
    public static void main(String[] args) {
        testTopologicalSort();
        testCycleDetection();
        testCycleRejection();
        testGreedySchedule();
        testCompletedTopicIsExcluded();
        System.out.println("ALL TESTS PASSED");
    }

    private static Topic topic(String id, String name) {
        return new Topic(id, name, 3, 2.0,
                LocalDate.of(2026, 10, 1));
    }

    private static void testTopologicalSort() {
        SyllabusGraph g = new SyllabusGraph();
        g.addTopic(topic("A", "Basics"));
        g.addTopic(topic("B", "Advanced"));
        g.addDependency("A", "B");

        var order = g.topologicalOrder();
        assert order.get(0).getId().equals("A");
        assert order.get(1).getId().equals("B");
    }

    private static void testCycleDetection() {
        SyllabusGraph g = new SyllabusGraph();
        g.addTopic(topic("A", "A"));
        g.addTopic(topic("B", "B"));
        g.addTopic(topic("C", "C"));
        g.addDependency("A", "B");
        g.addDependency("B", "C");
        assert !g.hasCycle();
    }

    private static void testCycleRejection() {
        SyllabusGraph g = new SyllabusGraph();
        g.addTopic(topic("A", "A"));
        g.addTopic(topic("B", "B"));
        g.addDependency("A", "B");

        boolean rejected = false;
        try {
            g.addDependency("B", "A");
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        assert rejected;
    }

    private static void testGreedySchedule() {
        SyllabusGraph g = new SyllabusGraph();
        g.addTopic(topic("A", "Basics"));
        g.addTopic(topic("B", "Advanced"));
        g.addDependency("A", "B");

        var plan = new GreedyScheduler().generate(
                g, LocalDate.of(2026, 9, 16), 2.0);

        assert plan.getSessions().size() == 2;
        assert plan.getSessions().get(0).getTopic().getId().equals("A");
        assert plan.getSessions().get(1).getTopic().getId().equals("B");
    }

    private static void testCompletedTopicIsExcluded() {
        SyllabusGraph g = new SyllabusGraph();
        Topic a = topic("A", "Basics");
        a.setStatus(Status.COMPLETED);
        g.addTopic(a);
        g.addTopic(topic("B", "Advanced"));
        g.addDependency("A", "B");

        var plan = new GreedyScheduler().generate(
                g, LocalDate.of(2026, 9, 16), 2.0);

        assert plan.getSessions().size() == 1;
        assert plan.getSessions().get(0).getTopic().getId().equals("B");
    }
}
