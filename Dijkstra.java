import java.util.Arrays;

public class Dijkstra {
    private IPriorityQueue queue;
    
    public Dijkstra(IPriorityQueue queue){
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
                        if (!Q.isEmpty()){
                            Q.decreaseKey(v, alt);
                        }
                        // Actualizar la distancia en la cola
                        
                        //Q.add(new Node(v, distancias[v])); // En un heap real usar decreaseKey
                    }
                }
            }
        }

        /* 
        //imprime resultados
        System.out.println("Nodo\tDistancia desde la raíz\tPrevio");
        for (int i = 0; i < V; i++) {
            System.out.println(i + "\t\t" + distancias[i] + "\t\t" + previos[i]);
        }
        */

        double[] previosD = new double[previos.length];

        for (int i = 0; i < previos.length; i++){
            previosD[i] = previos[i]/1.0;
        }

        
        double[][] arreglos = new double[][]{distancias, previosD};
        return arreglos;
    }
}
