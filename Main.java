public class Main {
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

        IPriorityQueue queue =  new Heap();

        Dijkstra Dijkstra = new Dijkstra(queue);

        Dijkstra.dijkstra(graph, source);
        System.out.println("heap!");


    }
}
