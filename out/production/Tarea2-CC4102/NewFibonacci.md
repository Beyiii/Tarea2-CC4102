//fibonacci

public class FibonacciHeap implements IPriorityQueue {
    private Node min;
    private int size;
    private Map<Integer, Node> nodes;

    private static class Node {
        int vertex;
        double distance;
        int degree;
        Node parent;
        Node child;
        Node left;
        Node right;
        boolean mark;

        Node(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
            this.degree = 0;
            this.mark = false;
            this.left = this;
            this.right = this;
        }
    }

    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
        this.nodes = new HashMap<>();
    }

    @Override
    public void add(Node node) {
        Node newNode = new Node(node.vertex, node.distance);
        nodes.put(node.vertex, newNode);
        if (min == null) {
            min = newNode;
        } else {
            insertIntoRootList(newNode);
            if (newNode.distance < min.distance) {
                min = newNode;
            }
        }
        size++;
    }

    @Override
    public Node poll() {
        if (min == null) {
            throw new NoSuchElementException();
        }
        Node z = min;
        if (z.child != null) {
            Node x = z.child;
            do {
                Node next = x.right;
                insertIntoRootList(x);
                x.parent = null;
                x = next;
            } while (x != z.child);
        }
        removeFromRootList(z);
        if (z == z.right) {
            min = null;
        } else {
            min = z.right;
            consolidate();
        }
        size--;
        nodes.remove(z.vertex);
        return new Node(z.vertex, z.distance);
    }

    @Override
    public void decreaseKey(int vertex, double newDist) {
        Node x = nodes.get(vertex);
        if (x == null) {
            throw new NoSuchElementException();
        }
        if (newDist > x.distance) {
            throw new IllegalArgumentException("New key is greater than current key");
        }
        x.distance = newDist;
        Node y = x.parent;
        if (y != null && x.distance < y.distance) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x.distance < min.distance) {
            min = x;
        }
    }

    @Override
    public boolean isEmpty() {
        return min == null;
    }

    private void insertIntoRootList(Node node) {
        if (min == null) {
            min = node;
        } else {
            node.left = min;
            node.right = min.right;
            min.right.left = node;
            min.right = node;
        }
    }

    private void removeFromRootList(Node node) {
        if (node.right == node) {
            min = null;
        } else {
            node.left.right = node.right;
            node.right.left = node.left;
        }
    }

    private void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(size) / Math.log(2))) + 1;
        List<Node> array = new ArrayList<>(Collections.nCopies(arraySize, null));
        List<Node> rootNodes = new ArrayList<>();
        Node x = min;
        if (x != null) {
            do {
                rootNodes.add(x);
                x = x.right;
            } while (x != min);
        }
        for (Node w : rootNodes) {
            x = w;
            int d = x.degree;
            while (array.get(d) != null) {
                Node y = array.get(d);
                if (x.distance > y.distance) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                array.set(d, null);
                d++;
            }
            array.set(d, x);
        }
        min = null;
        for (Node y : array) {
            if (y != null) {
                if (min == null) {
                    min = y;
                } else {
                    insertIntoRootList(y);
                    if (y.distance < min.distance) {
                        min = y;
                    }
                }
            }
        }
    }

    private void link(Node y, Node x) {
        removeFromRootList(y);
        y.left = y;
        y.right = y;
        if (x.child == null) {
            x.child = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        y.parent = x;
        x.degree++;
        y.mark = false;
    }

    private void cut(Node x, Node y) {
        if (x.right == x) {
            y.child = null;
        } else {
            x.left.right = x.right;
            x.right.left = x.left;
            if (y.child == x) {
                y.child = x.right;
            }
        }
        y.degree--;
        insertIntoRootList(x);
        x.parent = null;
        x.mark = false;
    }

    private void cascadingCut(Node y) {
        Node z = y.parent;
        if (z != null) {
            if (!y.mark) {
                y.mark = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }
}


==================================================================================================================================

//Dijkstra


public class Dijkstra {
    private IPriorityQueue queue;

    public Dijkstra(IPriorityQueue queue) {
        this.queue = queue;
    }

    public double[][] dijkstra(double[][] graph, int source) {
        int V = graph.length;
        double[] distancias = new double[V];
        int[] previos = new int[V];
        IPriorityQueue Q = queue;

        // Inicialización
        Arrays.fill(distancias, Double.MAX_VALUE);
        Arrays.fill(previos, -1);
        distancias[source] = 0;
        Q.add(new Node(source, 0));

        // Agregar todos los nodos a la cola con distancia infinita excepto la raíz
        for (int v = 0; v < V; v++) {
            if (v != source) {
                Q.add(new Node(v, Double.MAX_VALUE));
            }
        }

        // Mientras la cola no esté vacía
        while (!Q.isEmpty()) {
            // Obtener el nodo con menor distancia
            Node node = Q.poll();
            int u = node.vertex;

            // Relajación
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0) { // Hay una arista entre u y v
                    double alt = distancias[u] + graph[u][v];
                    if (alt < distancias[v]) {
                        distancias[v] = alt;
                        previos[v] = u;

                        // Actualizar la distancia en la cola
                        Q.decreaseKey(v, alt);
                    }
                }
            }
        }

        double[] previosD = new double[previos.length];

        for (int i = 0; i < previos.length; i++) {
            previosD[i] = previos[i] / 1.0;
        }

        double[][] arreglos = new double[][]{distancias, previosD};
        return arreglos;
    }
}


========================================================================================================================================
