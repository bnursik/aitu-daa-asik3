package app;

import graph.Graph;
import algo.Prim;
import algo.Kruskal;
import algo.MSTResult;
import io.JsonIO;

import java.io.*;
import java.util.*;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java -cp target/classes app.Main <input.json> <output.json> [summary.csv]");
                return;
            }

            String inputPath = args[0];
            String outputPath = args[1];
            String csvPath = (args.length >= 3 ? args[2] : null);

            // read input graphs
            List<JsonIO.InputGraph> graphs = JsonIO.readInput(inputPath);
            if (graphs.isEmpty()) {
                System.out.println("No graphs found in input: " + inputPath + " (expected { \"graphs\": [...] })");
                // still write an empty results file for consistency
                JsonIO.writeOutput(outputPath, new ArrayList<>());
                if (csvPath != null)
                    writeCsvHeader(csvPath);
                return;
            }

            List<JsonIO.OutputGraphResult> results = new ArrayList<>();
            List<String> csvLines = new ArrayList<>();
            if (csvPath != null) {
                csvLines.add(
                        "Graph_ID,Vertices,Edges,Prim_Cost,Kruskal_Cost,Prim_Time(ms),Kruskal_Time(ms),Prim_Ops,Kruskal_Ops,Faster,Costs_Match");
            }

            for (JsonIO.InputGraph ig : graphs) {
                Graph g = JsonIO.toGraph(ig);

                MSTResult primRes = Prim.run(g);
                MSTResult kruskalRes = Kruskal.run(g);

                JsonIO.OutputGraphResult out = new JsonIO.OutputGraphResult();
                out.graph_id = ig.id;
                out.input_stats = new JsonIO.InputStats();
                out.input_stats.vertices = g.getVertexCount();
                out.input_stats.edges = g.getEdges().size();
                out.prim = JsonIO.packAlgo(primRes, g);
                out.kruskal = JsonIO.packAlgo(kruskalRes, g);
                results.add(out);

                boolean costsMatch = primRes.connected && kruskalRes.connected &&
                        Math.abs(primRes.totalCost - kruskalRes.totalCost) < 1e-9;
                String faster = primRes.timeMs <= kruskalRes.timeMs ? "Prim" : "Kruskal";

                System.out.printf(
                        Locale.US,
                        "Graph %d  |  V=%d E=%d  |  Prim=%.3f (%.2f ms, ops=%d)  |  Kruskal=%.3f (%.2f ms, ops=%d)  |  %s  %s%n",
                        ig.id,
                        g.getVertexCount(),
                        g.getEdges().size(),
                        primRes.totalCost, (double) primRes.timeMs, sumOps(primRes.operations),
                        kruskalRes.totalCost, (double) kruskalRes.timeMs, sumOps(kruskalRes.operations),
                        costsMatch ? "✅ costs match" : "⚠️ costs differ",
                        "faster=" + faster);

                if (csvPath != null) {
                    csvLines.add(String.format(
                            Locale.US,
                            "%d,%d,%d,%.6f,%.6f,%.6f,%.6f,%d,%d,%s,%s",
                            ig.id,
                            g.getVertexCount(),
                            g.getEdges().size(),
                            primRes.totalCost,
                            kruskalRes.totalCost,
                            (double) primRes.timeMs,
                            (double) kruskalRes.timeMs,
                            sumOps(primRes.operations),
                            sumOps(kruskalRes.operations),
                            faster,
                            costsMatch ? "true" : "false"));
                }
            }

            // write JSON output (your required schema)
            JsonIO.writeOutput(outputPath, results);
            System.out.println("✅ Results written to " + outputPath);

            // write CSV summary if requested
            if (csvPath != null) {
                writeCsv(csvPath, csvLines);
                System.out.println("✅ Summary CSV written to " + csvPath + "  (rows=" + (csvLines.size() - 1) + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int sumOps(Map<String, Integer> ops) {
        if (ops == null)
            return 0;
        int s = 0;
        for (Integer v : ops.values())
            if (v != null)
                s += v;
        return s;
    }

    private static void writeCsvHeader(String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            pw.println(
                    "Graph_ID,Vertices,Edges,Prim_Cost,Kruskal_Cost,Prim_Time(ms),Kruskal_Time(ms),Prim_Ops,Kruskal_Ops,Faster,Costs_Match");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeCsv(String path, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            for (String s : lines)
                pw.println(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
    