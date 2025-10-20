package algo;

import graph.Edge;
import java.util.*;

public class MSTResult {
    public List<Edge> mstEdges = new ArrayList<>();
    public double totalCost = 0.0;
    public long timeMs = 0L;
    public boolean connected = true;
    public Map<String, Integer> operations = new HashMap<>();
}
