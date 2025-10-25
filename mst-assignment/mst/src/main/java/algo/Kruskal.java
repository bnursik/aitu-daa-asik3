package algo;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class Kruskal {
    public static MSTResult run(Graph g) {
        MSTResult res = new MSTResult();
        int n = g.getVertexCount();
        List<Edge> edges = new ArrayList<>(g.getEdges());
        final int[] sortComps = { 0 };
        Collections.sort(edges, (a, b) -> {
            sortComps[0]++;
            return Double.compare(a.getWeight(), b.getWeight());
        });
        DisjointSet dsu = new DisjointSet(n);
        long t0 = System.nanoTime();
        for (Edge e : edges) {
            int u = e.getU();
            int v = e.getV();
            if (dsu.find(u) != dsu.find(v)) {
                dsu.union(u, v);
                res.mstEdges.add(e);
                res.totalCost += e.getWeight();
                if (res.mstEdges.size() == n - 1)
                    break;
            }
        }
        long t1 = System.nanoTime();
        res.timeMs = Math.round((t1 - t0) / 1_000);
        res.connected = (res.mstEdges.size() == Math.max(0, n - 1));
        Map<String, Integer> ops = new HashMap<>();
        ops.put("sortComparisons", sortComps[0]);
        ops.put("finds", dsu.findOps);
        ops.put("unions", dsu.unionOps);
        res.operations = ops;
        return res;
    }
}
