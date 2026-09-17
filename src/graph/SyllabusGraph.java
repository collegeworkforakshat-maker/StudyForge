package graph;

import model.Topic;

import java.util.*;

public class SyllabusGraph {
    private final Map<String, Topic> topics = new LinkedHashMap<>();
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();

    public void addTopic(Topic topic) {
        if (topics.containsKey(topic.getId())) {
            throw new IllegalArgumentException("Topic ID already exists: " + topic.getId());
        }
        topics.put(topic.getId(), topic);
        adjacency.put(topic.getId(), new LinkedHashSet<>());
    }

    public void removeTopic(String id) {
        if (!topics.containsKey(id)) throw new IllegalArgumentException("Topic not found: " + id);
        topics.remove(id);
        adjacency.remove(id);
        for (Set<String> edges : adjacency.values()) edges.remove(id);
    }

    public void addDependency(String prerequisiteId, String dependentId) {
        if (!topics.containsKey(prerequisiteId) || !topics.containsKey(dependentId)) {
            throw new IllegalArgumentException("Both topics must exist.");
        }
        if (prerequisiteId.equals(dependentId)) {
            throw new IllegalArgumentException("A topic cannot depend on itself.");
        }

        adjacency.get(prerequisiteId).add(dependentId);

        if (hasCycle()) {
            adjacency.get(prerequisiteId).remove(dependentId);
            throw new IllegalArgumentException("Dependency rejected: it creates a cycle.");
        }
    }

    public List<Topic> topologicalOrder() {
        Map<String, Integer> indegree = new LinkedHashMap<>();
        for (String id : topics.keySet()) indegree.put(id, 0);

        for (Set<String> edges : adjacency.values()) {
            for (String to : edges) indegree.put(to, indegree.get(to) + 1);
        }

        Queue<String> queue = new ArrayDeque<>();
        for (String id : topics.keySet()) {
            if (indegree.get(id) == 0) queue.offer(id);
        }

        List<Topic> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String id = queue.poll();
            result.add(topics.get(id));

            for (String next : adjacency.get(id)) {
                indegree.put(next, indegree.get(next) - 1);
                if (indegree.get(next) == 0) queue.offer(next);
            }
        }

        if (result.size() != topics.size()) {
            throw new IllegalStateException("Graph contains a cycle.");
        }
        return result;
    }

    public boolean hasCycle() {
        Set<String> visiting = new HashSet<>();
        Set<String> visited = new HashSet<>();

        for (String id : topics.keySet()) {
            if (dfsCycle(id, visiting, visited)) return true;
        }
        return false;
    }

    private boolean dfsCycle(String id, Set<String> visiting, Set<String> visited) {
        if (visiting.contains(id)) return true;
        if (visited.contains(id)) return false;

        visiting.add(id);
        for (String next : adjacency.get(id)) {
            if (dfsCycle(next, visiting, visited)) return true;
        }
        visiting.remove(id);
        visited.add(id);
        return false;
    }

    public List<Topic> getPrerequisites(String topicId) {
        List<Topic> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : adjacency.entrySet()) {
            if (entry.getValue().contains(topicId)) result.add(topics.get(entry.getKey()));
        }
        return result;
    }

    public List<Topic> getTopics() {
        return new ArrayList<>(topics.values());
    }

    public Map<String, Set<String>> getAdjacency() {
        return Collections.unmodifiableMap(adjacency);
    }

    public void printGraph() {
        if (topics.isEmpty()) {
            System.out.println("Graph is empty.");
            return;
        }
        for (String id : topics.keySet()) {
            System.out.print(id + " -> ");
            System.out.println(adjacency.get(id).isEmpty() ? "(none)" : String.join(", ", adjacency.get(id)));
        }
    }
}
