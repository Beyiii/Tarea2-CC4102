import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Experiment {
    IPriorityQueue queueH =  new Heap();
    IPriorityQueue queueF =  new Fibonacci();
    Dijkstra DijkstraH = new Dijkstra(queueH);
    Dijkstra DijkstraF = new Dijkstra(queueF);

    public void experimentHeap(int i, int j, Random rand, BufferedWriter writer) throws IOException {
        int v = (int) Math.pow(2, i);  
        int e = (int) Math.pow(2, j);

        double[][] graph = GraphGenerator.generateGraph(v, e, rand); // rand: semilla para que el grafo sea distinto siempre

        long startTime = System.nanoTime();
        DijkstraH.dijkstra(graph, 0);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        String result = "Heap: v=" + v + ", e=" + e + ", Time (µs)=" + TimeUnit.NANOSECONDS.toMicros(duration);
        System.out.println(result);
        writer.write(result);
        writer.newLine(); 
    }

    public void experimentFibonacci(int i, int j, Random rand, BufferedWriter writer) throws IOException {
        int v = (int) Math.pow(2, i);
        int e = (int) Math.pow(2, j);

        double[][] graph = GraphGenerator.generateGraph(v, e, rand); // rand: semilla para que el grafo sea distinto siempre

        long startTime = System.nanoTime();
        DijkstraF.dijkstra(graph, 0);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        String result = "Fibonacci: v=" + v + ", e=" + e + ", Time (µs)=" + TimeUnit.NANOSECONDS.toMicros(duration);
        System.out.println(result);
        writer.write(result);
        writer.newLine();
    }



    public void finalExperiment(int n) {
        int[] vList = {10, 12, 14};
        int[] eList = {16, 17, 18, 19, 20, 21, 22};
        int contadorHeap = 0;
        int contadorFibonacci = 0;
        int contadorFallos = 0;

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try (BufferedWriter writerHeap = new BufferedWriter(new FileWriter("experimentos_heap.txt"));
             BufferedWriter writerFibonacci = new BufferedWriter(new FileWriter("experimentos_fibonacci.txt"))) {

            for (int k = 0; k < n; k++) {
                for (int i : vList) {
                    for (int j : eList) {
                        boolean success = false;
                        while (!success) {

                            Random r = new Random(System.currentTimeMillis());
                            experimentHeap(i, j, r, writerHeap);

                            Future<?> future = executor.submit(() -> {
                                try {
                                    experimentFibonacci(i, j, r, writerFibonacci);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            try {
                                future.get(1500, TimeUnit.MILLISECONDS);
                                success = true;
                            } catch (TimeoutException e) {
                                System.out.println("Retrying Fibonacci due to timeout with a new seed...");
                                contadorFallos++;
                                future.cancel(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                success = true; // Exit on unexpected exception
                            }
                        }
                        contadorHeap++;
                        contadorFibonacci++;
                    }
                }
            }
            writerHeap.write("n de grafos recorridos: " + contadorHeap);
            writerHeap.newLine();
            System.out.println("Heap - n de grafos recorridos: " + contadorHeap);

            writerFibonacci.write("n de grafos recorridos: " + contadorFibonacci);
            writerFibonacci.newLine();
            System.out.println("Fibonacci - n de grafos recorridos: " + contadorFibonacci);

            writerFibonacci.write("n de fallos: " + contadorFallos);
            writerFibonacci.newLine();
            System.out.println("Fibonacci - n de fallos: " + contadorFallos);
        } catch (IOException e) {
            System.err.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
        }

        executor.shutdown();
        System.out.println("Heap - n de grafos recorridos: " + contadorHeap);
        System.out.println("Fibonacci - n de grafos recorridos: " + contadorFibonacci);
    }

    /**
    public void experimentFibonacci(int i, int j, Random rand, BufferedWriter writer) throws IOException {
        int v = (int) Math.pow(2, i);
        int e = (int) Math.pow(2, j);

        double[][] graph = GraphGenerator.generateGraph(v, e, rand); // rand: semilla para que el grafo sea distinto siempre

        long startTime = System.nanoTime();
        DijkstraF.dijkstra(graph, 0);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        String result = "Fibonacci: v=" + v + ", e=" + e + ", Time (µs)=" + TimeUnit.NANOSECONDS.toMicros(duration);
        System.out.println(result);
        writer.write(result);
        writer.newLine();
    }

    public void finalExperimentFibonacci(int n) {
        int[] vList = {10, 12, 14};
        int[] eList = {16, 17, 18, 19, 20, 21, 22};
        int contadorFibonacci = 0;


        ExecutorService executor = Executors.newSingleThreadExecutor();

        try (BufferedWriter writerFibonacci = new BufferedWriter(new FileWriter("experimentos.txt"))) {
            for (int k = 0; k < n; k++) {
                for (int i : vList) {
                    for (int j : eList) {
                        boolean success = false;
                        while (!success) {

                            Random rand = new Random(System.currentTimeMillis());

                            Future<?> future = executor.submit(() -> {
                                try {
                                    experimentFibonacci(i, j, rand, writerFibonacci);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            try {
                                future.get(1500, TimeUnit.MILLISECONDS);
                                success = true;
                            } catch (TimeoutException e) {
                                System.out.println("Retrying due to timeout with a new seed...");
                                future.cancel(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                success = true; // Exit on unexpected exception
                            }
                        }
                        contadorFibonacci++;
                    }
                }
            }
            writerFibonacci.write("n de grafos recorridos: " + contadorFibonacci);
            writerFibonacci.newLine();
            System.out.println("Fibonacci - n de grafos recorridos: " + contadorFibonacci);
        } catch (IOException e) {
            System.err.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
        }

        executor.shutdown();
    }
  */
}


/*
    public void experimentFibonacci(int i, int j, Random rand) {
        int v = (int) Math.pow(2, i);  
        int e = (int) Math.pow(2, j);

        double[][] graph = GraphGenerator.generateGraph(v, e, rand); // rand: semilla para que el grafo sea distinto siempre

        long startTime = System.nanoTime();
        DijkstraF.dijkstra(graph, 0);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        System.out.println("v=" + v + ", e=" + e + ", Time (ms)=" + TimeUnit.NANOSECONDS.toMillis(duration)); 
    }

    public void finalExperimentHeap(int n){
        int[] vList = {10,12,14};
        int[] eList = {16,17,18,19,20,21,22};
        int contador = 0;
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("experimentos.txt"))) {
            for (int k = 0; k < n; k++) {
                long seed = System.nanoTime() + k;
                Random rand = new Random(seed);
            
                for (int i : vList) {
                    for (int j : eList) {
                        experimentHeap(i, j, rand, writer);
                        contador++;
                    }
                }
            }
            writer.write("n de grafos recorridos: " + contador);
            writer.newLine();
            System.out.println("n de grafos recorridos: " + contador);
        } catch (IOException e) {
            System.err.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
        }

        System.out.println("n de grafos recorridos: " + contador); 
    }

    public void finalExperimentFibonacci(int n) {
        int[] vList = {10, 12, 14};
        int[] eList = {16, 17, 18, 19, 20, 21, 22};
        int contador = 0;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        for (int k = 0; k < n; k++) {
            for (int i : vList) {
                for (int j : eList) {
                    boolean success = false;
                    while (!success) {

                        Random rand = new Random(System.currentTimeMillis());

                        Future<?> future = executor.submit(() -> experimentFibonacci(i, j, rand));
                        try {
                            future.get(1500, TimeUnit.MILLISECONDS);
                            success = true;
                        } catch (TimeoutException e) {
                            System.out.println("Retrying due to timeout with a new seed...");
                            future.cancel(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            success = true; // Exit on unexpected exception
                        }
                    }
                    contador++;
                }
            }
        }

        executor.shutdown();
        System.out.println("n de grafos recorridos: " + contador); 
    }


*/
