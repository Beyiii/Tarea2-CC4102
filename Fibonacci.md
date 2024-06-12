## Cola de Fibonacci

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

class FibonacciHeap implements IPriorityQueue {
    private Node min;
    private int size;
    private Map<Integer, Node> nodes;

    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
        this.nodes = new HashMap<>();
    }

    private static class Node {
        int vertex;
        double key;
        int degree;
        Node parent;
        Node child;
        Node left;
        Node right;
        boolean mark;

        Node(int vertex, double key) {
            this.vertex = vertex;
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
            this.mark = false;
        }
    }

    @Override
    public void add(Node node) {
        Node fNode = new Node(node.vertex, node.distance);
        if (min != null) {
            fNode.left = min;
            fNode.right = min.right;
            min.right = fNode;
            fNode.right.left = fNode;
            if (node.distance < min.key) {
                min = fNode;
            }
        } else {
            min = fNode;
        }
        nodes.put(node.vertex, fNode);
        size++;
    }

    @Override
    public Node poll() {
        Node z = min;
        if (z != null) {
            if (z.child != null) {
                Node child = z.child;
                do {
                    child.parent = null;
                    child = child.right;
                } while (child != z.child);
                Node minLeft = min.left;
                Node zChildLeft = z.child.left;
                min.left = zChildLeft;
                zChildLeft.right = min;
                z.child.left = minLeft;
                minLeft.right = z.child;
            }
            z.left.right = z.right;
            z.right.left = z.left;
            if (z == z.right) {
                min = null;
            } else {
                min = z.right;
                consolidate();
            }
            size--;
            nodes.remove(z.vertex);
        }
        if (z == null) {
            throw new NoSuchElementException("The heap is empty");
        }
        return new Node(z.vertex, z.key);
    }

    private void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(size) / Math.log(2))) + 1;
        Node[] array = new Node[arraySize];
        Node start = min;
        Node w = min;
        do {
            Node x = w;
            int d = x.degree;
            while (array[d] != null) {
                Node y = array[d];
                if (x.key > y.key) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                array[d] = null;
                d++;
            }
            array[d] = x;
            w = w.right;
        } while (w != start);
        min = null;
        for (int i = 0; i < arraySize; i++) {
            if (array[i] != null) {
                if (min != null) {
                    array[i].left.right = array[i].right;
                    array[i].right.left = array[i].left;
                    array[i].left = min;
                    array[i].right = min.right;
                    min.right = array[i];
                    array[i].right.left = array[i];
                    if (array[i].key < min.key) {
                        min = array[i];
                    }
                } else {
                    min = array[i];
                }
            }
        }
    }

    private void link(Node y, Node x) {
        y.left.right = y.right;
        y.right.left = y.left;
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }
        x.degree++;
        y.mark = false;
    }

    @Override
    public boolean isEmpty() {
        return min == null;
    }

    @Override
    public void decreaseKey(int vertex, double newKey) {
        Node x = nodes.get(vertex);
        if (x == null) {
            throw new NoSuchElementException("Node not found in the heap");
        }
        if (newKey > x.key) {
            throw new IllegalArgumentException("New key is greater than current key");
        }
        x.key = newKey;
        Node y = x.parent;
        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x.key < min.key) {
            min = x;
        }
    }

    private void cut(Node x, Node y) {
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;
        if (y.child == x) {
            y.child = x.right;
        }
        if (y.degree == 0) {
            y.child = null;
        }
        x.left = min;
        x.right = min.right;
        min.right = x;
        x.right.left = x;
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

Implementación y Complejidad Amortizada
La implementación de decreaseKey que se detalló anteriormente sigue estos pasos, y su tiempo de ejecución amortizado es O(1). Este tiempo amortizado constante se debe a la naturaleza de las operaciones y al manejo de los cortes en cascada.

============================================================================================================
Para implementar el algoritmo de Dijkstra utilizando una cola de Fibonacci como estructura de datos para la cola de prioridad, primero necesitamos definir la estructura de la cola de Fibonacci. Posteriormente, podemos integrarla en el algoritmo de Dijkstra sin modificar la lógica del algoritmo.

Implementación de la Cola de Fibonacci
Vamos a implementar la cola de Fibonacci que permite las operaciones de insert, extractMin, y decreaseKey en el tiempo requerido. La cola de Fibonacci se describe detalladamente en el libro "Introduction to Algorithms" de Cormen et al.

Definición de la Cola de Fibonacci
A continuación, se presenta la implementación de una cola de Fibonacci en Java:

java
Copiar código
import java.util.*;

public class FibonacciHeap {

    private Node min;
    private int size;

    public FibonacciHeap() {
        min = null;
        size = 0;
    }

    public void insert(Node node) {
        if (min == null) {
            min = node;
        } else {
            mergeLists(min, node);
            if (node.key < min.key) {
                min = node;
            }
        }
        size++;
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
            size--;
        }
        return extractedMin;
    }

    public void decreaseKey(Node node, double newKey) {
        if (newKey > node.key) {
            throw new IllegalArgumentException("New key is greater than current key");
        }
        node.key = newKey;
        Node parent = node.parent;
        if (parent != null && node.key < parent.key) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.key < min.key) {
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
                if (node.key > other.key) {
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
                    if (node.key < min.key) {
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

    public static class Node {
        private Node left, right, parent, child;
        private double key;
        private int degree;
        private boolean mark;
        private int vertex;

        public Node(int vertex, double key) {
            this.vertex = vertex;
            this.key = key;
            this.left = this;
            this.right = this;
            this.parent = null;
            this.child = null;
            this.degree = 0;
            this.mark = false;
        }

        public int getVertex() {
            return vertex;
        }

        public double getKey() {
            return key;
        }
    }
}
Uso de la Cola de Fibonacci en el Algoritmo de Dijkstra
Ahora podemos utilizar esta implementación de la cola de Fibonacci en el algoritmo de Dijkstra:

java
Copiar código
import java.util.*;

public class DijkstraFibonacciHeap {

    public static void dijkstra(double[][] graph, int source) {
        int V = graph.length; // Número de nodos en el grafo
        double[] distancias = new double[V]; // Array para almacenar distancias mínimas
        int[] previos = new int[V]; // Array para almacenar los nodos previos en el camino más corto
        FibonacciHeap Q = new FibonacciHeap(); // Cola de prioridad para seleccionar el nodo con menor distancia
        FibonacciHeap.Node[] nodes = new FibonacciHeap.Node[V];

        // Inicialización
        Arrays.fill(distancias, Double.MAX_VALUE); // Inicializa todas las distancias a infinito
        Arrays.fill(previos, -1); // Inicializa todos los nodos previos a -1
        distancias[source] = 0; // La distancia del nodo raíz a sí mismo es 0

        // Agregar todos los nodos a la cola con la distancia correspondiente
        for (int v = 0; v < V; v++) {
            nodes[v] = new FibonacciHeap.Node(v, distancias[v]);
            Q.insert(nodes[v]);
        }

        // Mientras la cola no esté vacía
        while (!Q.isEmpty()) {
            // Obtener el nodo con menor distancia
            FibonacciHeap.Node node = Q.extractMin();
            int u = node.getVertex();

            // Relajación
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0) { // Hay una arista entre u y v
                    double alt = distancias[u] + graph[u][v];
                    if (alt < distancias[v]) {
                        distancias[v] = alt;
                        previos[v] = u;
                        Q.decreaseKey(nodes[v], alt);
                    }
                }
            }
        }

        // Imprimir resultados
        System.out.println("Nodo\tDistancia desde la raíz\tPrevio");
        for (int i = 0; i < V; i++) {
            System.out.println(i + "\t\t" + distancias[i] + "\t\t" + previos[i]);
        }
    }

    public static void main(String[] args) {
        // Ejemplo de grafo
        double[][] graph = {
            {0, 0.1, 0, 0.3, 1.0},
            {0.1, 0, 0.5, 0, 0},
            {0, 0.5, 0, 0.2, 0.1},
            {0.3, 0, 0.2, 0, 0.6},
            {1.0, 0, 0.1, 0.6, 0},
        };
        int source = 0; // Nodo raíz

        // Llamada al método estático dijkstra sin necesidad de instancia
        DijkstraFibonacciHeap.dijkstra(graph, source);
    }
}
Resumen
Este código define una implementación completa de una cola de Fibonacci y la integra con el algoritmo de Dijkstra. La cola de Fibonacci permite una operación decreaseKey en tiempo constante, lo que mejora la eficiencia del algoritmo de Dijkstra, especialmente en grafos con muchas aristas y nodos. Con esta implementación, el algoritmo de Dijkstra tiene una complejidad de O(e+vlogv), donde e es el número de aristas y v es el número de nodos.
