package graph;

public class Edge implements Comparable<Edge> {
    private final int u;
    private final int v;
    private final double weight;

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public double getWeight() {
        return weight;
    }

    public int other(int vertex) {
        if (vertex == u)
            return v;
        else if (vertex == v)
            return u;
        else
            throw new IllegalArgumentException("Vertex not part of the edge");
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d -- %d, %.2f)", u, v, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Edge))
            return false;
        Edge that = (Edge) obj;
        return (this.u == that.u && this.v == that.v || this.u == that.v && this.v == that.u)
                && Double.compare(this.weight, that.weight) == 0;
    }

    @Override
    public int hashCode() {
        int mini = Math.min(u, v);
        int maxi = Math.max(u, v);
        long bits = Double.doubleToLongBits(weight);
        return (31 * mini + maxi) ^ (int) (bits ^ (bits >>> 32));
    }

}