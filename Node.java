public class Node implements Comparable<Node> {
    int vertex;
    double distance;

    Node(int vertex, double distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.distance, other.distance);
    }
}