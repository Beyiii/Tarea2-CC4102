import java.util.*;

public class Fibonacci implements IPriorityQueue {

    private Node min;
    private int size;
    private Map<Integer, Node> nodeMap; // Mapa para seguimiento de nodos por su vértice

    public Fibonacci() {
        min = null;
        size = 0;
        nodeMap = new HashMap<>();
    }

  
    public void add(Node node) {
        insert(node);
    }

    public void insert(Node node) {
        if (min == null) {
            min = node;
        } else {
            mergeLists(min, node);
            if (node.distance < min.distance) {
                min = node;
            }
        }
        nodeMap.put(node.vertex, node); // Agregar al mapa
        size++;
    }


    public Node poll() {
        return extractMin();
    }

    public Node extractMin() {
        Node extractedMin = min;
        if (extractedMin != null) {
            if (extractedMin.child != null) {
                Node child = extractedMin.child;
                do {
                    Node nextChild = child.right;
                    mergeLists(min, child);
                    child.parent = null;
                    child = nextChild;
                } while (child != extractedMin.child);
            }
            removeNodeFromList(extractedMin);
            if (extractedMin == extractedMin.right) {
                min = null;
            } else {
                min = extractedMin.right;
                consolidate();
            }
            nodeMap.remove(extractedMin.vertex); // Eliminar del mapa
            size--;
        }
        return extractedMin;
    }


    public void decreaseKey(int vertex, double newKey) {
        Node node = nodeMap.get(vertex); // Obtener el nodo desde el mapa
        if (node == null) {
            throw new IllegalArgumentException("Node with the given vertex does not exist");
        }
        if (newKey > node.distance) {
            throw new IllegalArgumentException("New key is greater than current key");
        }
        node.distance = newKey;
        Node parent = node.parent;
        if (parent != null && node.distance < parent.distance) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (min == null || node.distance < min.distance) {
            min = node;
        }
    }


    public boolean isEmpty() {
        return min == null;
    }

    private void mergeLists(Node a, Node b) {
        if (a == null || b == null) return;
        Node aRight = a.right;
        a.right = b.right;
        a.right.left = a;
        b.right = aRight;
        b.right.left = b;
    }

    private void removeNodeFromList(Node node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void cut(Node node, Node parent) {
        removeNodeFromList(node);
        parent.degree--;
        if (node == node.right) {
            parent.child = null;
        } else {
            parent.child = node.right;
        }
        node.right = node.left = node;
        mergeLists(min, node);
        node.parent = null;
        node.mark = false;
    }

    private void cascadingCut(Node node) {
        Node parent = node.parent;
        if (parent != null) {
            if (!node.mark) {
                node.mark = true;
            } else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }

    private void consolidate() {
        List<Node> aux = new ArrayList<>(45); // Max degree can be log(phi) of number of nodes
        for (int i = 0; i < 45; i++) {
            aux.add(null);
        }
        List<Node> toVisit = new ArrayList<>();
        Node start = min;
        if (start != null) {
            Node current = start;
            do {
                toVisit.add(current);
                current = current.right;
            } while (current != start);
        }

        for (Node node : toVisit) {
            int degree = node.degree;
            while (aux.get(degree) != null) {
                Node other = aux.get(degree);
                if (node.distance > other.distance) {
                    Node temp = node;
                    node = other;
                    other = temp;
                }
                link(other, node);
                aux.set(degree, null);
                degree++;
            }
            aux.set(degree, node);
        }

        min = null;
        for (Node node : aux) {
            if (node != null) {
                if (min == null) {
                    min = node;
                } else {
                    mergeLists(min, node);
                    if (node.distance < min.distance) {
                        min = node;
                    }
                }
            }
        }
    }

    private void link(Node y, Node x) {
        removeNodeFromList(y);
        y.left = y.right = y;
        mergeLists(x.child, y);
        y.parent = x;
        x.child = y;
        x.degree++;
        y.mark = false;
    }
}