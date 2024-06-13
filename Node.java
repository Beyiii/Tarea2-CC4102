public class Node implements Comparable<Node> {
    Node left, right, parent, child;
    double distance;
    int degree;
    boolean mark;
    int vertex;

    Node(int vertex, double distance) {
        this.vertex = vertex;
        this.distance = distance;
        this.left = this;
        this.right = this;
        this.parent = null;
        this.child = null;
        this.degree = 0;
        this.mark = false;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.distance, other.distance);
    }
}