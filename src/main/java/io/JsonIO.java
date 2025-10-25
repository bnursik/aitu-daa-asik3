package io;

import com.google.gson.*;
import java.io.*;
import java.util.*;

import graph.Graph;
import graph.Edge;
import algo.MSTResult;

public class JsonIO {

    // === Input structures ===
    public static class InputEdge {
        public String from;
        public String to;
        public double weight;
    }

    public static class InputGraph {
        public int id;
        public List<String> nodes;
        public List<InputEdge> edges;
    }

    public static class InputFile {
        public List<InputGraph> graphs;
    }

    // === Output structures ===
    public static class OutputGraphResult {
        public int graph_id;
        public InputStats input_stats;
        public AlgorithmResult prim;
        public AlgorithmResult kruskal;
    }

    public static class InputStats {
        public int vertices;
        public int edges;
    }

    public static class AlgorithmResult {
        public List<EdgeData> mst_edges = new ArrayList<>();
        public double total_cost;
        public int operations_count;
        public double execution_time_ms;
    }

    public static class EdgeData {
        public String from;
        public String to;
        public double weight;
    }

    public static class OutputFile {
        public List<OutputGraphResult> results = new ArrayList<>();
    }

    // === Read input file ===
    public static List<InputGraph> readInput(String path) throws IOException {
        try (Reader reader = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            InputFile f = gson.fromJson(reader, InputFile.class);
            if (f == null || f.graphs == null)
                return List.of();
            return f.graphs;
        }
    }

    // === Convert input graph to Graph object for algorithms ===
    public static Graph toGraph(InputGraph g) {
        List<String> vertices = g.nodes;
        List<Map<String, Object>> edges = new ArrayList<>();
        for (InputEdge e : g.edges) {
            edges.add(Map.of("u", e.from, "v", e.to, "w", e.weight));
        }
        return Graph.fromData(vertices, edges);
    }

    // === Build output JSON ===
    public static AlgorithmResult packAlgo(MSTResult res, Graph g) {
        AlgorithmResult ar = new AlgorithmResult();
        for (graph.Edge e : res.mstEdges) {
            EdgeData ed = new EdgeData();
            ed.from = g.getName(e.getU());
            ed.to = g.getName(e.getV());
            ed.weight = e.getWeight();
            ar.mst_edges.add(ed);
        }
        ar.total_cost = res.totalCost;
        ar.operations_count = res.operations.values().stream().mapToInt(Integer::intValue).sum();
        ar.execution_time_ms = res.timeMs;
        return ar;
    }

    public static void writeOutput(String path, List<OutputGraphResult> results) throws IOException {
        OutputFile file = new OutputFile();
        file.results = results;
        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(file, writer);
        }
    }
}
