import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de grafo
        double[][] graph = {
            {0.0, 0.01, 0.0, 0.03, 0.1},
            {0.01, 0.0, 0.05, 0.0, 0.0},
            {0.0, 0.05, 0.0, 0.02, 0.01},
            {0.03, 0.0, 0.02, 0.0, 0.06},
            {0.1, 0.0, 0.01, 0.06, 0.0},
        };
        int source = 0; // Nodo raíz

        IPriorityQueue queue =  new Heap();

        Dijkstra Dijkstra = new Dijkstra(queue);

        Dijkstra.dijkstra(graph, source);
        System.out.println("heap!");

        Experiment e = new Experiment();
        Random rand = new Random(System.currentTimeMillis());
        e.experimentHeap(10,20, rand);
        //e.finalExperimentHeap(1);


    }
}
