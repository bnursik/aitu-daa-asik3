package algo;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class Prim {
    public static MSTResult run(Graph g) {
        MSTResult res = new MSTResult();
        int n = g.getVertexCount();
        boolean[] visited = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        long t0 = System.nanoTime();
        int start = 0;
        visited[start] = true;
        pq.addAll(g.getAdjacencyList().get(start));
        int visitedCount = 1;
        int heapOps = pq.size();
        int edgeRelaxations = 0;
        int comparisons = 0;

        while (!pq.isEmpty() && res.mstEdges.size() < n - 1) {
            Edge e = pq.poll();
            comparisons++;
            int u = e.getU(), v = e.getV();
            int next = visited[u] ? v : u;
            if (visited[next])
                continue;
            visited[next] = true;
            res.mstEdges.add(e);
            res.totalCost += e.getWeight();
            visitedCount++;
            for (Edge adjEdge : g.getAdjacencyList().get(next)) {
                int other = adjEdge.other(next);
                if (!visited[other]) {
                    pq.add(adjEdge);
                    heapOps++;
                    edgeRelaxations++;
                }
            }
        }

        long t1 = System.nanoTime();
        res.timeMs = Math.round((t1 - t0) / 1_000);
        res.connected = (visitedCount == n);
        Map<String, Integer> ops = new HashMap<>();
        ops.put("heapOps", heapOps);
        ops.put("edgeRelaxations", edgeRelaxations);
        ops.put("comparisons", comparisons);
        res.operations = ops;
        return res;
    }
}
