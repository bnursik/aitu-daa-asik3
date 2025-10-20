import graph.Graph;
import java.util.*;

/**
 * Simple test driver for the Graph and Edge classes.
 * Later you’ll expand this to include Prim’s and Kruskal’s algorithms.
 */
public class Main {
    public static void main(String[] args) {
        // 1️⃣ Example: city districts (vertices)
        List<String> vertices = Arrays.asList("A", "B", "C", "D");

        // 2️⃣ Example: possible roads (edges)
        List<Map<String, Object>> edgesData = new ArrayList<>();
        edgesData.add(Map.of("u", "A", "v", "B", "w", 3));
        edgesData.add(Map.of("u", "A", "v", "C", "w", 1));
        edgesData.add(Map.of("u", "B", "v", "C", "w", 7));
        edgesData.add(Map.of("u", "C", "v", "D", "w", 2));
        edgesData.add(Map.of("u", "B", "v", "D", "w", 5));

        // 3️⃣ Create the graph from data
        Graph g = Graph.fromData(vertices, edgesData);

        // 4️⃣ Print it to check structure
        System.out.println(g);

        // 5️⃣ Optional: show adjacency list for Prim’s later
        System.out.println("Adjacency list:");
        for (int i = 0; i < g.getVertexCount(); i++) {
            System.out.print("Vertex " + i + " (" + g.getName(i) + "): ");
            for (var e : g.getAdjacencyList().get(i)) {
                System.out.print(e + " ");
            }
            System.out.println();
        }
    }
}
