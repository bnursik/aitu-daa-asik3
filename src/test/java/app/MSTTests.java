package app;

import graph.Graph;
import algo.Prim;
import algo.Kruskal;
import algo.MSTResult;
import io.JsonIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MSTTests {

    static Graph smallConnected, disconnected;

    @BeforeAll
    static void setupGraphs() {
        // === small connected graph ===
        List<String> V = Arrays.asList("A", "B", "C", "D", "E");
        List<Map<String, Object>> E = new ArrayList<>();
        E.add(Map.of("u", "A", "v", "B", "w", 4));
        E.add(Map.of("u", "A", "v", "C", "w", 3));
        E.add(Map.of("u", "B", "v", "C", "w", 2));
        E.add(Map.of("u", "B", "v", "D", "w", 5));
        E.add(Map.of("u", "C", "v", "D", "w", 7));
        E.add(Map.of("u", "C", "v", "E", "w", 8));
        E.add(Map.of("u", "D", "v", "E", "w", 6));
        smallConnected = Graph.fromData(V, E);

        // === disconnected graph (two components) ===
        List<String> V2 = Arrays.asList("A", "B", "C", "D");
        List<Map<String, Object>> E2 = new ArrayList<>();
        E2.add(Map.of("u", "A", "v", "B", "w", 1));
        E2.add(Map.of("u", "C", "v", "D", "w", 2));
        disconnected = Graph.fromData(V2, E2);
    }

    // ---------- CORRECTNESS TESTS ----------

    @Test
    @DisplayName("MST total cost identical for Prim and Kruskal")
    void testTotalCostEqual() {
        MSTResult p = Prim.run(smallConnected);
        MSTResult k = Kruskal.run(smallConnected);
        assertEquals(p.totalCost, k.totalCost, 1e-9, "Total cost must be identical");
    }

    @Test
    @DisplayName("Each MST has V-1 edges")
    void testEdgeCountVMinus1() {
        MSTResult p = Prim.run(smallConnected);
        MSTResult k = Kruskal.run(smallConnected);
        assertEquals(smallConnected.getVertexCount() - 1, p.mstEdges.size());
        assertEquals(smallConnected.getVertexCount() - 1, k.mstEdges.size());
    }

    @Test
    @DisplayName("MST is connected (single component)")
    void testConnected() {
        MSTResult p = Prim.run(smallConnected);
        MSTResult k = Kruskal.run(smallConnected);
        assertTrue(p.connected, "Prim MST should connect all vertices");
        assertTrue(k.connected, "Kruskal MST should connect all vertices");
    }

    @Test
    @DisplayName("MST is acyclic")
    void testAcyclic() {
        MSTResult p = Prim.run(smallConnected);
        assertTrue(isAcyclic(smallConnected.getVertexCount(), p.mstEdges));
    }

    // simple union-find check for cycles
    private boolean isAcyclic(int vCount, List<graph.Edge> edges) {
        int[] parent = new int[vCount];
        for (int i = 0; i < vCount; i++)
            parent[i] = i;
        java.util.function.IntUnaryOperator find = new java.util.function.IntUnaryOperator() {
            @Override
            public int applyAsInt(int x) {
                return parent[x] == x ? x : (parent[x] = applyAsInt(parent[x]));
            }
        };
        for (graph.Edge e : edges) {
            int u = find.applyAsInt(e.getU());
            int v = find.applyAsInt(e.getV());
            if (u == v)
                return false; // cycle
            parent[u] = v;
        }
        return true;
    }

    @Test
    @DisplayName("Disconnected graph handled gracefully (no MST)")
    void testDisconnectedHandled() {
        MSTResult p = Prim.run(disconnected);
        MSTResult k = Kruskal.run(disconnected);
        assertFalse(p.connected);
        assertFalse(k.connected);
        assertTrue(p.mstEdges.size() < disconnected.getVertexCount() - 1);
        assertTrue(k.mstEdges.size() < disconnected.getVertexCount() - 1);
    }

    // ---------- PERFORMANCE / CONSISTENCY TESTS ----------

    @Test
    @DisplayName("Execution time non-negative and reproducible")
    void testExecutionTimeAndReproducibility() {
        MSTResult p1 = Prim.run(smallConnected);
        MSTResult p2 = Prim.run(smallConnected);
        assertTrue(p1.timeMs >= 0);
        assertTrue(p2.timeMs >= 0);
        assertEquals(p1.totalCost, p2.totalCost, 1e-9);
    }

    @Test
    @DisplayName("Operation counts non-negative")
    void testOperationCountsNonNegative() {
        MSTResult p = Prim.run(smallConnected);
        MSTResult k = Kruskal.run(smallConnected);
        assertTrue(p.operations.values().stream().allMatch(v -> v >= 0));
        assertTrue(k.operations.values().stream().allMatch(v -> v >= 0));
    }

    @Test
    @DisplayName("Prim and Kruskal results reproducible for same dataset")
    void testReproducibility() {
        MSTResult p1 = Prim.run(smallConnected);
        MSTResult p2 = Prim.run(smallConnected);
        MSTResult k1 = Kruskal.run(smallConnected);
        MSTResult k2 = Kruskal.run(smallConnected);
        assertEquals(p1.totalCost, p2.totalCost, 1e-9);
        assertEquals(k1.totalCost, k2.totalCost, 1e-9);
    }
}
