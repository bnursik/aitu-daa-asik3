package io;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import graph.Graph;
import algo.MSTResult;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonIO {
    public static class InputGraph {
        public String name;
        public List<String> vertices;
        public List<Map<String, Object>> edges;
        public Map<String, Object> meta;
    }

    public static class InputFile {
        public List<InputGraph> graphs;
    }

    public static class OutputItem {
        public String graph;
        public int vertexCount;
        public int edgeCount;
        public String status;
        public Map<String, Object> prim;
        public Map<String, Object> kruskal;
    }

    public static class OutputFile {
        public List<OutputItem> results = new ArrayList<>();
    }

    public static List<InputGraph> readInput(String path) throws IOException {
        try (Reader r = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            InputFile f = gson.fromJson(r, InputFile.class);
            return f.graphs == null ? List.of() : f.graphs;
        }
    }

    public static void writeOutput(String path, List<OutputItem> items) throws IOException {
        try (Writer w = new BufferedWriter(new FileWriter(path))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            OutputFile out = new OutputFile();
            out.results = items;
            gson.toJson(out, w);
        }
    }

    public static Graph toGraph(InputGraph ig) {
        return Graph.fromData(ig.vertices, ig.edges);
    }

    public static Map<String, Object> packAlgo(String algoName, MSTResult r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalCost", r.totalCost);
        m.put("timeMs", r.timeMs);
        m.put("operations", r.operations);
        List<Map<String, Object>> edges = new ArrayList<>();
        for (var e : r.mstEdges) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("u", e.getU());
            row.put("v", e.getV());
            row.put("w", e.getWeight());
            edges.add(row);
        }
        m.put("mstEdges", edges);
        m.put("connected", r.connected);
        return m;
    }
}
