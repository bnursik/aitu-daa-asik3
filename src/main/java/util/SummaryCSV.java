package util;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SummaryCSV {

    private static JsonObject readJson(String path) throws Exception {
        try (Reader r = new BufferedReader(new FileReader(path))) {
            return JsonParser.parseReader(r).getAsJsonObject();
        }
    }

    private static void writeCsv(String path, List<String> lines) throws Exception {
        if (path.equals("-")) {
            // print to stdout if user passed "-" as path
            for (String s : lines)
                System.out.println(s);
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            for (String s : lines)
                pw.println(s);
        }
    }

    public static void main(String[] args) {
        System.out.println("SummaryCSV v2");
        try {
            if (args.length < 2) {
                System.out.println("Usage: java util.SummaryCSV <OUTPUT.json> <SUMMARY.csv|->");
                return;
            }
            String in = Paths.get(args[0]).toAbsolutePath().toString();
            String out = args[1].equals("-") ? "-" : Paths.get(args[1]).toAbsolutePath().toString();

            System.out.println("INPUT JSON : " + in);
            System.out.println("OUTPUT CSV : " + out);

            File f = new File(in);
            if (!f.exists() || f.length() == 0) {
                System.err.println("❌ Input JSON not found or empty.");
                return;
            }

            JsonObject root = readJson(in);

            if (!root.has("results")) {
                System.err.println("❌ Missing top-level 'results' array. Is this the OUTPUT json?");
                return;
            }

            JsonArray results = root.getAsJsonArray("results");
            System.out.println("results.size = " + results.size());

            if (results.size() == 0) {
                System.err.println("⚠️ 'results' is empty. Nothing to summarize.");
                // still write header so file isn't blank
                writeCsv(out, List.of(
                        "Graph_ID,Vertices,Edges,Prim_Cost,Kruskal_Cost,Prim_Time(ms),Kruskal_Time(ms),Prim_Ops,Kruskal_Ops,Faster"));
                return;
            }

            List<String> lines = new ArrayList<>();
            lines.add(
                    "Graph_ID,Vertices,Edges,Prim_Cost,Kruskal_Cost,Prim_Time(ms),Kruskal_Time(ms),Prim_Ops,Kruskal_Ops,Faster");

            int printedPreview = 0;

            for (JsonElement el : results) {
                JsonObject g = el.getAsJsonObject();

                int id = g.get("graph_id").getAsInt();

                JsonObject stats = g.getAsJsonObject("input_stats");
                int vertices = stats.get("vertices").getAsInt();
                int edges = stats.get("edges").getAsInt();

                JsonObject prim = g.getAsJsonObject("prim");
                JsonObject kruskal = g.getAsJsonObject("kruskal");

                double pc = prim.get("total_cost").getAsDouble();
                double kc = kruskal.get("total_cost").getAsDouble();
                double pt = prim.get("execution_time_ms").getAsDouble();
                double kt = kruskal.get("execution_time_ms").getAsDouble();
                int pops = prim.get("operations_count").getAsInt();
                int kops = kruskal.get("operations_count").getAsInt();
                String faster = pt <= kt ? "Prim" : "Kruskal";

                lines.add(String.format(Locale.US,
                        "%d,%d,%d,%.6f,%.6f,%.6f,%.6f,%d,%d,%s",
                        id, vertices, edges, pc, kc, pt, kt, pops, kops, faster));

                if (printedPreview < 3) {
                    System.out.printf(Locale.US,
                            "preview row -> id=%d V=%d E=%d PC=%.2f KC=%.2f PT=%.2f KT=%.2f%n",
                            id, vertices, edges, pc, kc, pt, kt);
                    printedPreview++;
                }
            }

            writeCsv(out, lines);
            System.out.println("✅ Wrote CSV rows: " + (lines.size() - 1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
