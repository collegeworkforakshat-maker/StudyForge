package persistence;

import graph.SyllabusGraph;
import model.Status;
import model.Topic;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class PersistenceManager {
    private static final String FILE = "studyforge_data.txt";

    public void save(SyllabusGraph graph) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE))) {
            writer.write("#TOPICS");
            writer.newLine();

            for (Topic t : graph.getTopics()) {
                writer.write(String.join("|",
                        t.getId(),
                        t.getName().replace("|", "/"),
                        String.valueOf(t.getDifficulty()),
                        String.valueOf(t.getEstimatedHours()),
                        t.getDeadline().toString(),
                        t.getStatus().name()));
                writer.newLine();
            }

            writer.write("#DEPENDENCIES");
            writer.newLine();

            for (Map.Entry<String, Set<String>> entry : graph.getAdjacency().entrySet()) {
                for (String child : entry.getValue()) {
                    writer.write(entry.getKey() + "|" + child);
                    writer.newLine();
                }
            }
        }
    }

    public SyllabusGraph load() throws IOException {
        SyllabusGraph graph = new SyllabusGraph();
        Path path = Paths.get(FILE);
        if (!Files.exists(path)) return graph;

        List<String> lines = Files.readAllLines(path);
        boolean topicsSection = false;
        boolean dependencySection = false;

        List<String[]> dependencies = new ArrayList<>();

        for (String line : lines) {
            if (line.equals("#TOPICS")) {
                topicsSection = true;
                dependencySection = false;
                continue;
            }
            if (line.equals("#DEPENDENCIES")) {
                topicsSection = false;
                dependencySection = true;
                continue;
            }
            if (line.isBlank()) continue;

            if (topicsSection) {
                String[] p = line.split("\\|", -1);
                if (p.length != 6) continue;
                Topic t = new Topic(p[0], p[1], Integer.parseInt(p[2]),
                        Double.parseDouble(p[3]), LocalDate.parse(p[4]));
                t.setStatus(Status.valueOf(p[5]));
                graph.addTopic(t);
            } else if (dependencySection) {
                String[] p = line.split("\\|", -1);
                if (p.length == 2) dependencies.add(p);
            }
        }

        for (String[] d : dependencies) graph.addDependency(d[0], d[1]);
        return graph;
    }
}
