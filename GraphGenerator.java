import java.util.*;

public class GraphGenerator {

    public static double[][] generateGraph(int v, int e, Random rand) {
        double[][] graph = new double[v][v];

        // Crear un árbol cobertor para asegurar la conectividad
        for (int i = 1; i < v; i++) {
            int j = rand.nextInt(i);
            double weight = rand.nextDouble() * 0.99999 + 0.00001; // Pesos aleatorios entre (0, 1]
            graph[i][j] = graph[j][i] = weight;
        }

        // Añadir las aristas restantes
        int edgesAdded = v - 1;
        while (edgesAdded < e) {
            int i = rand.nextInt(v);
            int j = rand.nextInt(v);
            if (i != j) { // VERLOOO
                double weight = rand.nextDouble() * 0.99999 + 0.00001; // Pesos aleatorios entre (0, 1]
                graph[i][j] = graph[j][i] = weight;
                edgesAdded++;
            }
        }

        return graph;
    }
}

/**
 * 
Para permitir que entre dos nodos exista más de una arista y evitar el bucle infinito en el while, podemos modificar la lógica de generación de aristas adicionales. Una vez que se ha garantizado la conectividad del grafo, podemos permitir que se agreguen aristas adicionales sin restringirnos a una sola arista entre cualquier par de nodos.

Aquí está la clase GraphGenerator modificada para cumplir con estos requisitos:

java
Copiar código
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
            if (i != j) {
                double weight = rand.nextDouble() * 0.99999 + 0.00001; // Pesos aleatorios entre (0, 1]
                graph[i][j] = graph[j][i] = weight;
                edgesAdded++;
            }
        }

        return graph;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int v = 16; // Número de nodos (ejemplo)
        int e = 256; // Número de aristas (ejemplo)

        double[][] graph = generateGraph(v, e, rand);

        // Imprimir el grafo generado
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < v; j++) {
                System.out.printf("%.5f ", graph[i][j]);
            }
            System.out.println();
        }
    }
}


Explicación de la Modificación
Conectividad Inicial:

Se mantiene el bucle for para asegurar que el grafo sea conexo. Esto se hace creando un árbol cobertor mínimo.
Cada nodo del grafo se conecta a al menos otro nodo garantizando la conectividad.
Añadir Aristas Adicionales:

En el bucle while que añade las aristas restantes, la condición se ha simplificado para permitir la creación de aristas adicionales entre cualquier par de nodos i y j (donde i≠j).
No se verifica si una arista ya existe entre i y j en el set existingEdges, permitiendo múltiples aristas entre nodos. Esto evita el problema del bucle infinito cuando se alcanzan las condiciones del grafo totalmente conectado.
Pesos de Aristas:

Los pesos se generan de manera aleatoria en el rango (0, 1] para cada arista.
Este cambio asegura que se generen hasta e aristas incluso si ya se han cubierto todas las posibles conexiones simples entre nodos, permitiendo múltiples aristas entre nodos para alcanzar el número deseado de aristas.



 */



