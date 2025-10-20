import graph.Graph;
import graph.Edge;
import algo.Prim;
import algo.Kruskal;
import algo.MSTResult;

import java.util.*;

public class Main {
    private static void printResult(String title, Graph g, MSTResult r) {
        System.out.println("=== " + title + " ===");
        System.out.println("Connected: " + r.connected);
        System.out.println("Total cost: " + r.totalCost);
        System.out.println("Time (ms): " + r.timeMs);
        System.out.println("Operations: " + r.operations);
        System.out.println("MST edges (" + r.mstEdges.size() + "):");
        for (Edge e : r.mstEdges) {
            String u = g.getName(e.getU());
            String v = g.getName(e.getV());
            System.out.println("  " + u + " -- " + v + " (" + e.getWeight() + ")");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Map<String, Object>> edgesData = new ArrayList<>();
        edgesData.add(Map.of("u", "A", "v", "B", "w", 3));
        edgesData.add(Map.of("u", "A", "v", "C", "w", 1));
        edgesData.add(Map.of("u", "B", "v", "C", "w", 7));
        edgesData.add(Map.of("u", "B", "v", "D", "w", 5));
        edgesData.add(Map.of("u", "C", "v", "D", "w", 2));

        Graph g = Graph.fromData(vertices, edgesData);

        MSTResult primRes = Prim.run(g);
        MSTResult kruskalRes = Kruskal.run(g);

        System.out.println("Graph: |V|=" + g.getVertexCount() + ", |E|=" + g.getEdges().size());
        printResult("Prim", g, primRes);
        printResult("Kruskal", g, kruskalRes);

        boolean costsMatch = Math.abs(primRes.totalCost - kruskalRes.totalCost) < 1e-9;
        System.out.println("MST total costs match: " + costsMatch);
    }
}
