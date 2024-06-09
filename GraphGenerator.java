import java.util.*;

public class GraphGenerator {

    public static double[][] generateGraph(int v, int e, Random rand) {
        double[][] graph = new double[v][v];
        Set<String> existingEdges = new HashSet<>();

        // Crear un árbol cobertor para asegurar la conectividad
        for (int i = 1; i < v; i++) {
            int j = rand.nextInt(i);
            double weight = rand.nextDouble() * 0.99999 + 0.00001; // Pesos aleatorios entre (0, 1]
            graph[i][j] = graph[j][i] = weight;
            existingEdges.add(i + "-" + j);
            existingEdges.add(j + "-" + i);
        }

        // Añadir las aristas restantes
        int edgesAdded = v - 1;
        while (edgesAdded < e) {
            int i = rand.nextInt(v);
            int j = rand.nextInt(v);
            if (i != j && graph[i][j] == 0 && !existingEdges.contains(i + "-" + j)) {
                double weight = rand.nextDouble() * 0.99999 + 0.00001; // Pesos aleatorios entre (0, 1]
                graph[i][j] = graph[j][i] = weight;
                existingEdges.add(i + "-" + j);
                existingEdges.add(j + "-" + i);
                edgesAdded++;
            }
        }

        return graph;
    }
}
