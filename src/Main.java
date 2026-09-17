import algorithm.*;
import graph.SyllabusGraph;
import model.*;
import persistence.PersistenceManager;
import service.ProgressTracker;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static SyllabusGraph graph = new SyllabusGraph();
    private static ProgressTracker progress = new ProgressTracker(graph);
    private static final PersistenceManager storage = new PersistenceManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();

        System.out.println("========================================");
        System.out.println("          STUDYFORGE v1.0");
        System.out.println(" Dependency-Aware Adaptive Study Planner");
        System.out.println("========================================");

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addTopic();
                    case "2" -> addDependency();
                    case "3" -> viewTopics();
                    case "4" -> viewGraph();
                    case "5" -> generatePlan();
                    case "6" -> updateProgress();
                    case "7" -> progress.printProgress();
                    case "8" -> saveData();
                    case "9" -> removeTopic();
                    case "10" -> explainAlgorithms();
                    case "0" -> {
                        saveData();
                        System.out.println("StudyForge closed. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--------------- MENU ----------------");
        System.out.println("1. Add topic");
        System.out.println("2. Add dependency");
        System.out.println("3. View topics");
        System.out.println("4. View dependency graph");
        System.out.println("5. Generate study plan");
        System.out.println("6. Update topic progress");
        System.out.println("7. View progress");
        System.out.println("8. Save data");
        System.out.println("9. Remove topic");
        System.out.println("10. Explain algorithms");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addTopic() {
        System.out.print("Topic ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Topic name: ");
        String name = scanner.nextLine().trim();

        int difficulty = readInt("Difficulty (1-5): ");
        double hours = readDouble("Estimated hours: ");

        System.out.print("Deadline (YYYY-MM-DD): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine().trim());

        graph.addTopic(new Topic(id, name, difficulty, hours, deadline));
        System.out.println("Topic added successfully.");
    }

    private static void addDependency() {
        System.out.print("Prerequisite topic ID: ");
        String prerequisite = scanner.nextLine().trim();

        System.out.print("Dependent topic ID: ");
        String dependent = scanner.nextLine().trim();

        graph.addDependency(prerequisite, dependent);
        System.out.println("Dependency added successfully. No cycle detected.");
    }

    private static void viewTopics() {
        if (graph.getTopics().isEmpty()) {
            System.out.println("No topics added.");
            return;
        }
        System.out.println("\nID | Topic | Details");
        for (Topic topic : graph.getTopics()) System.out.println(topic);
    }

    private static void viewGraph() {
        System.out.println("\nDependency Graph (Prerequisite -> Dependent):");
        graph.printGraph();
        System.out.println("Cycle present: " + graph.hasCycle());
        System.out.println("Topological order:");
        for (Topic t : graph.topologicalOrder()) {
            System.out.print(t.getId() + " ");
        }
        System.out.println();
    }

    private static void generatePlan() {
        if (graph.getTopics().isEmpty()) {
            System.out.println("Add topics first.");
            return;
        }

        double dailyHours = readDouble("Available study hours per day: ");
        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine().trim());

        System.out.println("\n1. Greedy Deadline-First");
        System.out.println("2. Critical-Path-Weighted");
        System.out.print("Choose scheduler: ");
        String choice = scanner.nextLine().trim();

        Scheduler scheduler = choice.equals("2")
                ? new CPMScheduler()
                : new GreedyScheduler();

        System.out.println("\nUsing: " + scheduler.getName());
        StudyPlan plan = scheduler.generate(graph, start, dailyHours);
        plan.printCalendar();

        System.out.println("\nTip: If progress changes, regenerate the plan to adapt.");
    }

    private static void updateProgress() {
        System.out.print("Topic ID: ");
        String id = scanner.nextLine().trim();

        System.out.println("1. NOT_STARTED");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. SKIPPED");
        int choice = readInt("Status: ");

        Status status = switch (choice) {
            case 1 -> Status.NOT_STARTED;
            case 2 -> Status.IN_PROGRESS;
            case 3 -> Status.COMPLETED;
            case 4 -> Status.SKIPPED;
            default -> throw new IllegalArgumentException("Invalid status.");
        };

        progress.updateStatus(id, status);
        System.out.println("Progress updated. Generate a new plan to recalculate the remaining schedule.");
    }

    private static void removeTopic() {
        System.out.print("Topic ID to remove: ");
        String id = scanner.nextLine().trim();
        graph.removeTopic(id);
        progress = new ProgressTracker(graph);
        System.out.println("Topic removed.");
    }

    private static void saveData() {
        try {
            storage.save(graph);
            System.out.println("Data saved to studyforge_data.txt.");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private static void loadData() {
        try {
            graph = storage.load();
            progress = new ProgressTracker(graph);
        } catch (Exception e) {
            graph = new SyllabusGraph();
            progress = new ProgressTracker(graph);
            System.out.println("Starting with empty data: " + e.getMessage());
        }
    }

    private static void explainAlgorithms() {
        System.out.println("\n--- StudyForge Algorithm Explanation ---");
        System.out.println("1. Adjacency List: stores prerequisite -> dependent relationships.");
        System.out.println("2. Cycle Detection: DFS prevents impossible circular prerequisites.");
        System.out.println("3. Topological Sort: Kahn's algorithm produces dependency-valid order.");
        System.out.println("4. Greedy Scheduler: prioritizes earliest deadlines, then difficulty.");
        System.out.println("5. CPM Scheduler: prioritizes topics with larger downstream critical weight.");
        System.out.println("6. Adaptive Planning: completed/skipped topics are removed from");
        System.out.println("   future scheduling, so regeneration creates an updated plan.");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}