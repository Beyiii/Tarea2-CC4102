import java.util.*;

public class MinHeap {
    // Clase interna para representar un nodo con su distancia
    static class Node implements Comparable<Node> {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    private Node[] heap;
    private int size;
    private int[] position; // Para rastrear la posición de cada nodo en el heap

    public MinHeap(int capacity) {
        heap = new Node[capacity];
        size = 0;
        position = new int[capacity];
        Arrays.fill(position, -1);
    }

    public void add(Node node) {
        heap[size] = node;
        position[node.vertex] = size;
        heapifyUp(size);
        size++;
    }

    public Node poll() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Node root = heap[0];
        Node lastNode = heap[size - 1];
        heap[0] = lastNode;
        position[lastNode.vertex] = 0;
        heap[size - 1] = null;
        size--;
        heapifyDown(0);
        position[root.vertex] = -1;
        return root;
    }

    public void decreaseKey(int vertex, int newDist) {
        int idx = position[vertex];
        Node node = heap[idx];
        node.distance = newDist;
        heapifyUp(idx);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void heapifyUp(int idx) {
        while (idx > 0) {
            int parentIdx = (idx - 1) / 2;
            if (heap[parentIdx].compareTo(heap[idx]) > 0) {
                swap(parentIdx, idx);
                idx = parentIdx;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int idx) {
        while (idx < size / 2) {
            int leftChildIdx = 2 * idx + 1;
            int rightChildIdx = 2 * idx + 2;
            int smallestIdx = idx;

            if (leftChildIdx < size && heap[leftChildIdx].compareTo(heap[smallestIdx]) < 0) {
                smallestIdx = leftChildIdx;
            }
            if (rightChildIdx < size && heap[rightChildIdx].compareTo(heap[smallestIdx]) < 0) {
                smallestIdx = rightChildIdx;
            }
            if (smallestIdx != idx) {
                swap(smallestIdx, idx);
                idx = smallestIdx;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        position[heap[i].vertex] = i;
        position[heap[j].vertex] = j;
    }
}


import java.util.*;

public class Dijkstra {

    // Método dijkstra declarado como estático
    public static void dijkstra(int[][] graph, int source) {
        int V = graph.length; // Número de nodos en el grafo
        int[] distancias = new int[V]; // Array para almacenar distancias mínimas
        int[] previos = new int[V]; // Array para almacenar los nodos previos en el camino más corto
        MinHeap Q = new MinHeap(V); // Cola de prioridad para seleccionar el nodo con menor distancia

        // Inicialización
        Arrays.fill(distancias, Integer.MAX_VALUE); // Inicializa todas las distancias a infinito
        Arrays.fill(previos, -1); // Inicializa todos los nodos previos a -1
        distancias[source] = 0; // La distancia del nodo raíz a sí mismo es 0
        Q.add(new MinHeap.Node(source, 0)); // Agrega el nodo raíz a la cola con distancia 0

        // Agregar todos los nodos a la cola con distancia infinita excepto la raíz
        for (int v = 0; v < V; v++) {
            if (v != source) {
                Q.add(new MinHeap.Node(v, Integer.MAX_VALUE));
            }
        }

        // Mientras la cola no esté vacía
        while (!Q.isEmpty()) {
            // Obtener el nodo con menor distancia
            MinHeap.Node node = Q.poll();
            int u = node.vertex;

            // Relajación
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0) { // Hay una arista entre u y v
                    int alt = distancias[u] + graph[u][v];
                    if (alt < distancias[v]) {
                        distancias[v] = alt;
                        previos[v] = u;

                        // Actualizar la distancia en la cola
                        Q.decreaseKey(v, alt); // En un heap real usar decreaseKey
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
        int[][] graph = {
            {0, 10, 0, 30, 100},
            {10, 0, 50, 0, 0},
            {0, 50, 0, 20, 10},
            {30, 0, 20, 0, 60},
            {100, 0, 10, 60, 0},
        };
        int source = 0; // Nodo raíz

        // Llamada al método estático dijkstra sin necesidad de instancia
        Dijkstra.dijkstra(graph, source);
    }
}


Explicación de MinHeap
add(Node node): Agrega un nuevo nodo al heap y ajusta la posición del nodo en el heap con heapifyUp.
poll(): Elimina y devuelve el nodo con la menor distancia (el nodo raíz del heap) y ajusta el heap con heapifyDown.
decreaseKey(int vertex, int newDist): Disminuye la distancia de un nodo específico y ajusta el heap con heapifyUp.
heapifyUp(int idx) y heapifyDown(int idx): Mantienen las propiedades del heap después de agregar o eliminar nodos.
swap(int i, int j): Intercambia dos nodos en el heap y actualiza sus posiciones.
Con esta implementación, el algoritmo de Dijkstra puede utilizar el MinHeap para obtener y actualizar las distancias de los nodos de manera eficiente.

=============================================================================================================


import java.util.*;

class MinHeap {
    private List<Node> heap;
    private Map<Integer, Integer> nodePosition;

    public MinHeap() {
        heap = new ArrayList<>();
        nodePosition = new HashMap<>();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void add(Node node) {
        heap.add(node);
        int currentIndex = heap.size() - 1;
        nodePosition.put(node.vertex, currentIndex);
        heapifyUp(currentIndex);
    }

    public Node poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }

        Node rootNode = heap.get(0);
        Node lastNode = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            nodePosition.put(lastNode.vertex, 0);
            heapifyDown(0);
        }

        nodePosition.remove(rootNode.vertex);
        return rootNode;
    }

    public void decreaseKey(int vertex, int newDistance) {
        int index = nodePosition.get(vertex);
        Node node = heap.get(index);
        node.distance = newDistance;
        heapifyUp(index);
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) >= 0) {
                break;
            }
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    private void heapifyDown(int index) {
        int size = heap.size();
        while (index < size) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int smallestIndex = index;

            if (leftChildIndex < size && heap.get(leftChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
                smallestIndex = leftChildIndex;
            }

            if (rightChildIndex < size && heap.get(rightChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
                smallestIndex = rightChildIndex;
            }

            if (smallestIndex == index) {
                break;
            }

            swap(index, smallestIndex);
            index = smallestIndex;
        }
    }

    private void swap(int index1, int index2) {
        Node node1 = heap.get(index1);
        Node node2 = heap.get(index2);
        heap.set(index1, node2);
        heap.set(index2, node1);
        nodePosition.put(node1.vertex, index2);
        nodePosition.put(node2.vertex, index1);
    }
}


===========================================================================================================

import java.util.*;

public class MinHeap {
    static class Node implements Comparable<Node> {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    private List<Node> heap;
    private Map<Integer, Integer> position; // Para rastrear la posición de cada nodo en el heap

    public MinHeap() {
        heap = new ArrayList<>();
        position = new HashMap<>();
    }

    public void add(Node node) {
        heap.add(node);
        int idx = heap.size() - 1;
        position.put(node.vertex, idx);
        heapifyUp(idx);
    }

    public Node poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        Node root = heap.get(0);
        Node lastNode = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            position.put(lastNode.vertex, 0);
            heapifyDown(0);
        }
        position.remove(root.vertex);
        return root;
    }

    public void decreaseKey(int vertex, int newDist) {
        Integer idx = position.get(vertex);
        if (idx == null) {
            throw new NoSuchElementException();
        }
        Node node = heap.get(idx);
        node.distance = newDist;
        heapifyUp(idx);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void heapifyUp(int idx) {
        while (idx > 0) {
            int parentIdx = (idx - 1) / 2;
            if (heap.get(parentIdx).compareTo(heap.get(idx)) > 0) {
                swap(parentIdx, idx);
                idx = parentIdx;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int idx) {
        int size = heap.size();
        while (idx < size / 2) {
            int leftChildIdx = 2 * idx + 1;
            int rightChildIdx = 2 * idx + 2;
            int smallestIdx = idx;

            if (leftChildIdx < size && heap.get(leftChildIdx).compareTo(heap.get(smallestIdx)) < 0) {
                smallestIdx = leftChildIdx;
            }
            if (rightChildIdx < size && heap.get(rightChildIdx).compareTo(heap.get(smallestIdx)) < 0) {
                smallestIdx = rightChildIdx;
            }
            if (smallestIdx != idx) {
                swap(smallestIdx, idx);
                idx = smallestIdx;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Node temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        position.put(heap.get(i).vertex, i);
        position.put(heap.get(j).vertex, j);
    }
}


import java.util.*;

public class Dijkstra {

    // Método dijkstra declarado como estático
    public static void dijkstra(int[][] graph, int source) {
        int V = graph.length; // Número de nodos en el grafo
        int[] distancias = new int[V]; // Array para almacenar distancias mínimas
        int[] previos = new int[V]; // Array para almacenar los nodos previos en el camino más corto
        MinHeap Q = new MinHeap(); // Cola de prioridad para seleccionar el nodo con menor distancia

        // Inicialización
        Arrays.fill(distancias, Integer.MAX_VALUE); // Inicializa todas las distancias a infinito
        Arrays.fill(previos, -1); // Inicializa todos los nodos previos a -1
        distancias[source] = 0; // La distancia del nodo raíz a sí mismo es 0
        Q.add(new MinHeap.Node(source, 0)); // Agrega el nodo raíz a la cola con distancia 0

        // Agregar todos los nodos a la cola con distancia infinita excepto la raíz
        for (int v = 0; v < V; v++) {
            if (v != source) {
                Q.add(new MinHeap.Node(v, Integer.MAX_VALUE));
            }
        }

        // Mientras la cola no esté vacía
        while (!Q.isEmpty()) {
            // Obtener el nodo con menor distancia
            MinHeap.Node node = Q.poll();
            int u = node.vertex;

            // Relajación
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0) { // Hay una arista entre u y v
                    int alt = distancias[u] + graph[u][v];
                    if (alt < distancias[v]) {
                        distancias[v] = alt;
                        previos[v] = u;

                        // Actualizar la distancia en la cola
                        Q.decreaseKey(v, alt); // En un heap real usar decreaseKey
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
        int[][] graph = {
            {0, 10, 0, 30, 100},
            {10, 0, 50, 0, 0},
            {0, 50, 0, 20, 10},
            {30, 0, 20, 0, 60},
            {100, 0, 10, 60, 0},
        };
        int source = 0; // Nodo raíz

        // Llamada al método estático dijkstra sin necesidad de instancia
        Dijkstra.dijkstra(graph, source);
    }
}
