Paso 2: Generación de Grafos Sintéticos
Vamos a generar los grafos conforme a las especificaciones mencionadas:

java
Copiar código
import java.util.*;

public class GraphGenerator {

    public static int[][] generateGraph(int v, int e) {
        Random rand = new Random();
        int[][] graph = new int[v][v];

        // Crear un árbol cobertor para asegurar la conectividad
        for (int i = 1; i < v; i++) {
            int j = rand.nextInt(i);
            graph[i][j] = graph[j][i] = rand.nextInt(100) + 1; // Pesos aleatorios entre 1 y 100
        }

        // Añadir las aristas restantes
        int edgesAdded = v - 1;
        while (edgesAdded < e) {
            int i = rand.nextInt(v);
            int j = rand.nextInt(v);
            if (i != j && graph[i][j] == 0) {
                graph[i][j] = graph[j][i] = rand.nextInt(100) + 1;
                edgesAdded++;
            }
        }

        return graph;
    }
}

Paso 3: Ejecución de Experimentos
Vamos a ejecutar los experimentos utilizando los grafos generados y medir los tiempos de ejecución del algoritmo de Dijkstra. Aquí se presenta un ejemplo de cómo se puede hacer esto:

java
Copiar código
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Experiment {

    public static void main(String[] args) {
        int[] iValues = {10, 12, 14};
        int[] jValues = {16, 17, 18, 19, 20, 21, 22};
        int iterations = 50;

        for (int i : iValues) {
            int v = (int) Math.pow(2, i);

            for (int j : jValues) {
                int e = (int) Math.pow(2, j);

                List<Long> executionTimes = new ArrayList<>();

                for (int iter = 0; iter < iterations; iter++) {
                    int[][] graph = GraphGenerator.generateGraph(v, e);

                    long startTime = System.nanoTime();
                    Dijkstra.dijkstra(graph, 0);
                    long endTime = System.nanoTime();

                    long duration = endTime - startTime;
                    executionTimes.add(duration);
                }

                long averageTime = executionTimes.stream().mapToLong(Long::longValue).sum() / iterations;
                System.out.println("v=" + v + ", e=" + e + ", averageTime (ms)=" + TimeUnit.NANOSECONDS.toMillis(averageTime));
            }
        }
    }
}

Paso 4: Análisis de Resultados
Para el análisis de resultados, se pueden utilizar herramientas como Python para realizar la regresión lineal y graficar los tiempos de ejecución. Aquí te dejo un ejemplo de cómo podrías hacer esto usando Python:

python
Copiar código
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression

# Datos de ejemplo (v, e, averageTime)
data = [
    # Valores de (v, e, averageTime) obtenidos de los experimentos
    # (16, 65536, 50), ...
]

# Convertir datos a arrays numpy
v_values = np.array([d[0] for d in data])
e_values = np.array([d[1] for d in data])
times = np.array([d[2] for d in data])

# Realizar regresión lineal en términos de e
e_log = np.log(e_values).reshape(-1, 1)
reg = LinearRegression().fit(e_log, times)

# Obtener pendiente y ordenada al origen
slope = reg.coef_[0]
intercept = reg.intercept_

print(f"Pendiente: {slope}")
print(f"Ordenada al origen: {intercept}")

# Graficar resultados
plt.scatter(e_values, times, label='Datos experimentales')
plt.plot(e_values, reg.predict(e_log), color='red', label='Ajuste lineal')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Número de aristas (e)')
plt.ylabel('Tiempo promedio (ms)')
plt.title('Rendimiento del algoritmo de Dijkstra')
plt.legend()
plt.show()

Interpretación de la Pendiente
La pendiente obtenida de la regresión lineal debe reflejar la complejidad esperada O(elogv). Si los tiempos de ejecución siguen aproximadamente una línea recta en la gráfica de escala log-log, esto indica que el tiempo de ejecución del algoritmo se comporta según la complejidad teórica.

Conclusión
Siguiendo los pasos anteriores, podemos verificar y confirmar empíricamente la complejidad del algoritmo de Dijkstra usando un heap como cola de prioridad. Además, al realizar los experimentos y analizar los resultados, podemos visualizar y confirmar la relación esperada entre el número de aristas y el tiempo de ejecución.


============================================================================================================
Corrección 

Entendido, vamos a ajustar el código de generación de grafos para que los pesos de las aristas sean números aleatorios en el rango (0, 1] en lugar de [1, 100].

Paso 2: Generación de Grafos Sintéticos
Aquí está el código actualizado para generar grafos con pesos aleatorios en el rango (0, 1]:

java
Copiar código
import java.util.*;

public class GraphGenerator {

    public static double[][] generateGraph(int v, int e) {
        Random rand = new Random();
        double[][] graph = new double[v][v];

        // Crear un árbol cobertor para asegurar la conectividad
        for (int i = 1; i < v; i++) {
            int j = rand.nextInt(i);
            double weight = 1.0 * rand.nextInt(100000) / 100000 + 0.00001; // Pesos aleatorios entre (0, 1]
            graph[i][j] = graph[j][i] = weight;
        }

        // Añadir las aristas restantes
        int edgesAdded = v - 1;
        while (edgesAdded < e) {
            int i = rand.nextInt(v);
            int j = rand.nextInt(v);
            if (i != j && graph[i][j] == 0) {
                double weight = 1.0 * rand.nextInt(100000) / 100000 + 0.00001; // Pesos aleatorios entre (0, 1]
                graph[i][j] = graph[j][i] = weight;
                edgesAdded++;
            }
        }

        return graph;
    }
}
Paso 3: Ejecución de Experimentos
Vamos a actualizar el código de experimentos para trabajar con el nuevo formato de grafo con pesos en el rango (0, 1]:

java
Copiar código
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Experiment {

    public static void main(String[] args) {
        int[] iValues = {10, 12, 14};
        int[] jValues = {16, 17, 18, 19, 20, 21, 22};
        int iterations = 50;

        for (int i : iValues) {
            int v = (int) Math.pow(2, i);

            for (int j : jValues) {
                int e = (int) Math.pow(2, j);

                List<Long> executionTimes = new ArrayList<>();

                for (int iter = 0; iter < iterations; iter++) {
                    double[][] graph = GraphGenerator.generateGraph(v, e);

                    long startTime = System.nanoTime();
                    Dijkstra.dijkstra(graph, 0);
                    long endTime = System.nanoTime();

                    long duration = endTime - startTime;
                    executionTimes.add(duration);
                }

                long averageTime = executionTimes.stream().mapToLong(Long::longValue).sum() / iterations;
                System.out.println("v=" + v + ", e=" + e + ", averageTime (ms)=" + TimeUnit.NANOSECONDS.toMillis(averageTime));
            }
        }
    }
}
Dijkstra Algorithm Adaptation for double weights
Additionally, we need to adapt the Dijkstra's algorithm to handle double weights:

java
Copiar código
import java.util.*;

public class Dijkstra {

    // Método dijkstra declarado como estático
    public static void dijkstra(double[][] graph, int source) {
        int V = graph.length; // Número de nodos en el grafo
        double[] distancias = new double[V]; // Array para almacenar distancias mínimas
        int[] previos = new int[V]; // Array para almacenar los nodos previos en el camino más corto
        MinHeap Q = new MinHeap(); // Cola de prioridad para seleccionar el nodo con menor distancia

        // Inicialización
        Arrays.fill(distancias, Double.MAX_VALUE); // Inicializa todas las distancias a infinito
        Arrays.fill(previos, -1); // Inicializa todos los nodos previos a -1
        distancias[source] = 0; // La distancia del nodo raíz a sí mismo es 0
        Q.add(new MinHeap.Node(source, 0)); // Agrega el nodo raíz a la cola con distancia 0

        // Agregar todos los nodos a la cola con distancia infinita excepto la raíz
        for (int v = 0; v < V; v++) {
            if (v != source) {
                Q.add(new MinHeap.Node(v, Double.MAX_VALUE));
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
                    double alt = distancias[u] + graph[u][v];
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
        double[][] graph = {
            {0, 0.1, 0, 0.3, 1.0},
            {0.1, 0, 0.5, 0, 0},
            {0, 0.5, 0, 0.2, 0.1},
            {0.3, 0, 0.2, 0, 0.6},
            {1.0, 0, 0.1, 0.6, 0},
        };
        int source = 0; // Nodo raíz

        // Llamada al método estático dijkstra sin necesidad de instancia
        Dijkstra.dijkstra(graph, source);
    }
}
Paso 4: Análisis de Resultados
Los resultados se pueden analizar y graficar como se indicó anteriormente, asegurando que la ejecución del algoritmo con los grafos generados y los tiempos de ejecución reflejen la complejidad esperada O(elogv).

Resumen
Estos cambios aseguran que los grafos generados tienen pesos en el rango correcto y que el algoritmo de Dijkstra puede trabajar con estos pesos. La estructura de los experimentos y el análisis de los resultados se mantienen, permitiendo verificar empíricamente la complejidad del algoritmo.

==========================================================================================================

Para asegurarnos de que los grafos generados sean diferentes y válidos (es decir, que cumplan con las especificaciones de ser conexos y no dirigidos con pesos en el rango (0, 1]), necesitamos usar una semilla diferente para el generador de números aleatorios en cada iteración y verificar que no se repitan aristas ya creadas. A continuación, se presenta una versión actualizada del código que garantiza la generación de grafos diferentes en cada iteración.

Paso 2: Generación de Grafos Sintéticos
Actualizaremos el método generateGraph para garantizar que cada grafo generado sea diferente:

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
Paso 3: Ejecución de Experimentos
Vamos a actualizar el código de experimentos para asegurarnos de que los grafos generados en cada iteración sean diferentes. Esto se puede lograr inicializando el Random con diferentes semillas:

java
Copiar código
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Experiment {

    public static void main(String[] args) {
        int[] iValues = {10, 12, 14};
        int[] jValues = {16, 17, 18, 19, 20, 21, 22};
        int iterations = 50;

        for (int i : iValues) {
            int v = (int) Math.pow(2, i);

            for (int j : jValues) {
                int e = (int) Math.pow(2, j);

                List<Long> executionTimes = new ArrayList<>();

                for (int iter = 0; iter < iterations; iter++) {
                    Random rand = new Random(System.currentTimeMillis() + iter);
                    double[][] graph = GraphGenerator.generateGraph(v, e, rand);

                    long startTime = System.nanoTime();
                    Dijkstra.dijkstra(graph, 0);
                    long endTime = System.nanoTime();

                    long duration = endTime - startTime;
                    executionTimes.add(duration);
                }

                long averageTime = executionTimes.stream().mapToLong(Long::longValue).sum() / iterations;
                System.out.println("v=" + v + ", e=" + e + ", averageTime (ms)=" + TimeUnit.NANOSECONDS.toMillis(averageTime));
            }
        }
    }
}
Dijkstra Algorithm Adaptation for double weights
El algoritmo de Dijkstra no necesita cambios adicionales y puede manejar double weights como antes.

Paso 4: Análisis de Resultados
Como se mencionó anteriormente, se puede realizar el análisis de resultados utilizando herramientas como Python para realizar regresión lineal y graficar los tiempos de ejecución.

Resumen
Estos cambios aseguran que cada grafo generado es diferente en cada iteración del experimento, utilizando semillas diferentes para el generador de números aleatorios. El método generateGraph se asegura de que no haya aristas repetidas, y el algoritmo de Dijkstra puede manejar los pesos en el rango correcto (0, 1]. Esto garantiza la validez de los experimentos y permite obtener resultados significativos.