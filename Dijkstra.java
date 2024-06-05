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
