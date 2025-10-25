package graph;

import java.util.*;

public class Graph {
    private final int vertexCount;
    private final List<Edge> edges;
    private final List<List<Edge>> adj;
    private final Map<String, Integer> nameToIndex;
    private final Map<Integer, String> indexToName;

    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>(vertexCount);
        this.nameToIndex = new HashMap<>();
        this.indexToName = new HashMap<>();
        for (int i = 0; i < vertexCount; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, double weight) {
        Edge edge = new Edge(u, v, weight);
        edges.add(edge);
        adj.get(u).add(edge);
        adj.get(v).add(edge);
    }

    public int addVertex(String name) {
        int index = nameToIndex.size();
        nameToIndex.put(name, index);
        indexToName.put(index, name);
        return index;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adj;
    }

    public int getIndex(String name) {
        return nameToIndex.getOrDefault(name, -1);
    }

    public String getName(int index) {
        return indexToName.getOrDefault(index, "unknown");
    }

    public static Graph fromData(List<String> vertices, List<Map<String, Object>> edgesData) {
        Graph g = new Graph(vertices.size());
        for (String v : vertices)
            g.addVertex(v);

        for (Map<String, Object> e : edgesData) {
            String uName = (String) e.get("u");
            String vName = (String) e.get("v");
            double w = ((Number) e.get("w")).doubleValue();
            int u = g.getIndex(uName);
            int v = g.getIndex(vName);
            g.addEdge(u, v, w);
        }
        return g;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertexCount)
                .append(" vertices and ").append(edges.size()).append(" edges:\n");
        for (Edge e : edges) {
            sb.append("  ").append(e.toString()).append("\n");
        }
        return sb.toString();
    }

}