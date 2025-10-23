import graph.Graph;
import algo.Prim;
import algo.Kruskal;
import algo.MSTResult;
import io.JsonIO;
import io.CsvIO;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println(
                        "Usage: java -cp out:lib/gson-2.10.1.jar Main <input.json> <output.json> <summary.csv>");
                return;
            }
            String inPath = args[0], outPath = args[1], csvPath = args[2];

            Object rawInputs = JsonIO.readInput(inPath);
            Iterable<?> inputsIterable;
            if (rawInputs instanceof Iterable) {
                inputsIterable = (Iterable<?>) rawInputs;
            } else if (rawInputs instanceof Object[]) {
                inputsIterable = Arrays.asList((Object[]) rawInputs);
            } else {
                inputsIterable = Collections.singletonList(rawInputs);
            }

            List<JsonIO.OutputItem> outItems = new ArrayList<>();
            List<String[]> csv = new ArrayList<>();
            csv.add(new String[] { "graph", "|V|", "|E|", "prim_cost", "kruskal_cost", "prim_ms", "kruskal_ms",
                    "prim_ops", "kruskal_ops", "status" });

            for (Object oig : inputsIterable) {
                JsonIO.Input ig = (JsonIO.Input) oig;
                Graph g = JsonIO.toGraph(ig);
                MSTResult prim = Prim.run(g);
                MSTResult kruskal = Kruskal.run(g);

                boolean costsMatch = prim.connected && kruskal.connected &&
                        Math.abs(prim.totalCost - kruskal.totalCost) < 1e-9;

                JsonIO.OutputItem item = new JsonIO.OutputItem();
                item.graph = ig.name;
                item.vertexCount = g.getVertexCount();
                item.edgeCount = g.getEdges().size();
                item.status = (prim.connected && kruskal.connected) ? "OK" : "DISCONNECTED";
                item.prim = JsonIO.packAlgo("prim", prim);
                item.kruskal = JsonIO.packAlgo("kruskal", kruskal);
                outItems.add(item);

                String primOps = String.valueOf(prim.operations.values().stream().mapToInt(Integer::intValue).sum());
                String kruskalOps = String
                        .valueOf(kruskal.operations.values().stream().mapToInt(Integer::intValue).sum());
                csv.add(new String[] {
                        ig.name,
                        String.valueOf(g.getVertexCount()),
                        String.valueOf(g.getEdges().size()),
                        String.valueOf(prim.totalCost),
                        String.valueOf(kruskal.totalCost),
                        String.valueOf(prim.timeMs),
                        String.valueOf(kruskal.timeMs),
                        primOps,
                        kruskalOps,
                        item.status + (costsMatch ? "" : "_COST_MISMATCH")
                });
            }

            JsonIO.writeOutput(outPath, outItems);
            CsvIO.writeSummary(csvPath, csv);
            System.out.println("Done. Wrote " + outItems.size() + " results.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
