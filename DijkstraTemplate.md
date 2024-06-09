import java.util.*;

public class Dijkstra {
    // Estructura auxiliar para representar un nodo con su distancia
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

    public void dijkstra(int[][] graph, int source) {
        int V = graph.length; // Número de nodos en el grafo
        int[] distancias = new int[V]; // Array para almacenar distancias mínimas
        int[] previos = new int[V]; // Array para almacenar los nodos previos en el camino más corto
        PriorityQueue<Node> Q = new PriorityQueue<>(); // Cola de prioridad para seleccionar el nodo con menor distancia

        // Inicialización
        Arrays.fill(distancias, Integer.MAX_VALUE); // Inicializa todas las distancias a infinito
        Arrays.fill(previos, -1); // Inicializa todos los nodos previos a -1
        distancias[source] = 0; // La distancia del nodo raíz a sí mismo es 0
        Q.add(new Node(source, 0)); // Agrega el nodo raíz a la cola con distancia 0

        // Agregar todos los nodos a la cola con distancia infinita excepto la raíz
        for (int v = 0; v < V; v++) {
            if (v != source) {
                Q.add(new Node(v, Integer.MAX_VALUE));
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
                    int alt = distancias[u] + graph[u][v];
                    if (alt < distancias[v]) {
                        distancias[v] = alt;
                        previos[v] = u;

                        // Actualizar la distancia en la cola
                        Q.add(new Node(v, distancias[v])); // En un heap real usar decreaseKey
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

        dijkstra(graph, source);
    }
}


Explicación del Código:
Clase Node: Representa un nodo en el grafo y su distancia desde el nodo raíz.
Método dijkstra:
Inicializa las distancias y previos para cada nodo.
Usa una cola de prioridad (PriorityQueue) para manejar los nodos a procesar.
Relaja las aristas y actualiza las distancias mínimas para cada nodo.
Main: Define un grafo de ejemplo y ejecuta el algoritmo de Dijkstra.
Esta implementación usa una PriorityQueue para representar la estructura Q y manejar las operaciones necesarias. Una vez que tengas la definición de las estructuras de heap y colas de Fibonacci, puedes adaptar la implementación para usar decreaseKey apropiadamente.


Visualización de una matriz de adyacencia

    0   1   2   3   4
0 [ 0, 10,  0, 30, 100]
1 [10,  0, 50,  0,   0]
2 [ 0, 50,  0, 20,  10]
3 [30,  0, 20,  0,  60]
4 [100, 0, 10, 60,   0]


==================================================================

Lo que teniamos antes de pasarlo a double 

import java.util.Arrays;

public class Dijkstra {
    private IPriorityQueue queue;
    
    public Dijkstra(IPriorityQueue queue){
        this.queue = queue;
    }
    
    public int[][] dijkstra(int[][] graph, int source) {
        int V = graph.length;
        int[] distancias = new int[V];
        int[] previos = new int[V];
        IPriorityQueue Q = queue;

        // Inicialización
        Arrays.fill(distancias, Integer.MAX_VALUE);
        Arrays.fill(previos, -1); 
        distancias[source] = 0;
        Q.add(new Node(source, 0));

        // Agregar todos los nodos a la cola con distancia infinita excepto la raíz
        for (int v = 0; v < V; v++) {
            if (v != source) {
                Q.add(new Node(v, Integer.MAX_VALUE));
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
                    int alt = distancias[u] + graph[u][v];
                    if (alt < distancias[v]) {
                        distancias[v] = alt;
                        previos[v] = u;

                        // Actualizar la distancia en la cola
                        Q.decreaseKey(v, alt);
                        //Q.add(new Node(v, distancias[v])); // En un heap real usar decreaseKey
                    }
                }
            }
        }

        // Imprimir resultados
        System.out.println("Nodo\tDistancia desde la raíz\tPrevio");
        for (int i = 0; i < V; i++) {
            System.out.println(i + "\t\t" + distancias[i] + "\t\t" + previos[i]);
        }

        
        int[][] arreglos = new int[][]{distancias, previos};
        return arreglos;
    }
}
