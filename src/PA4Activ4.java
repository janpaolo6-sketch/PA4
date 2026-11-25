import java.util.*;

public class PA4Activ4 {

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // ==========================================
    // 2. MÓDULO DE GRAFOS (Estructuras)
    // ==========================================
    // Clase Nodo (Estática para poder usarla dentro del main)
    static class Nodo {
        String nombre;
        int x, y;

        public Nodo(String nombre, int x, int y) {
            this.nombre = nombre;
            this.x = x;
            this.y = y;
        }

        // Heurística para A* (Distancia euclidiana)
        public double distanciaA(Nodo otro) {
            return Math.sqrt(Math.pow(this.x - otro.x, 2) + Math.pow(this.y - otro.y, 2));
        }
    }

    // Clase Grafo
    static class Grafo {
        Map<String, List<Edge>> adjList = new HashMap<>();
        Map<String, Nodo> nodos = new HashMap<>();

        public void agregarNodo(Nodo nodo) {
            nodos.put(nodo.nombre, nodo);
            adjList.putIfAbsent(nodo.nombre, new ArrayList<>());
        }

        public void agregarArista(String origen, String destino, int peso) {
            adjList.get(origen).add(new Edge(destino, peso));
            adjList.get(destino).add(new Edge(origen, peso)); // No dirigido
        }

        public List<Edge> getVecinos(String nodo) {
            return adjList.getOrDefault(nodo, new ArrayList<>());
        }
    }

    // Clase Arista
    static class Edge {
        String destino;
        int peso;

        public Edge(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ==========================================
    // 3. ALGORITMOS DE BÚSQUEDA
    // ==========================================

    // --- DFS (Búsqueda en Profundidad) ---
    public static void dfs(Grafo grafo, String inicio, String objetivo) {
        Set<String> visitados = new HashSet<>();
        List<String> camino = new ArrayList<>();
        boolean encontrado = dfsRecursivo(grafo, inicio, objetivo, visitados, camino);
        System.out.println("DFS - Camino encontrado: " + (encontrado ? camino : "No encontrado"));
    }

    private static boolean dfsRecursivo(Grafo grafo, String actual, String objetivo, Set<String> visitados, List<String> camino) {
        if (actual.equals(objetivo)) {
            camino.add(actual);
            return true;
        }
        visitados.add(actual);
        for (Edge edge : grafo.getVecinos(actual)) {
            if (!visitados.contains(edge.destino)) {
                camino.add(actual);
                if (dfsRecursivo(grafo, edge.destino, objetivo, visitados, camino)) return true;
                camino.remove(camino.size() - 1);
            }
        }
        return false;
    }

    // --- BFS (Búsqueda en Anchura) ---
    public static void bfs(Grafo grafo, String inicio, String objetivo) {
        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, String> padres = new HashMap<>();

        cola.add(inicio);
        visitados.add(inicio);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (actual.equals(objetivo)) {
                reconstruirCamino(padres, objetivo, "BFS");
                return;
            }
            for (Edge edge : grafo.getVecinos(actual)) {
                if (!visitados.contains(edge.destino)) {
                    visitados.add(edge.destino);
                    padres.put(edge.destino, actual);
                    cola.add(edge.destino);
                }
            }
        }
        System.out.println("BFS - No se encontró camino.");
    }

    // --- A* (Algoritmo Heurístico) ---
    static class NodeAEstrella {
        String nombre;
        double gScore;
        double fScore;
        NodeAEstrella(String nombre, double gScore, double fScore) {
            this.nombre = nombre;
            this.gScore = gScore;
            this.fScore = fScore;
        }
    }

    public static void aEstrella(Grafo grafo, String inicio, String objetivo) {
        PriorityQueue<NodeAEstrella> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fScore));
        Map<String, Double> gScore = new HashMap<>();
        Map<String, String> padres = new HashMap<>();
        Set<String> closedSet = new HashSet<>();

        Nodo nodoInicio = grafo.nodos.get(inicio);
        Nodo nodoObjetivo = grafo.nodos.get(objetivo);

        gScore.put(inicio, 0.0);
        openSet.add(new NodeAEstrella(inicio, 0, nodoInicio.distanciaA(nodoObjetivo)));

        while (!openSet.isEmpty()) {
            NodeAEstrella current = openSet.poll();
            String currentNombre = current.nombre;

            if (currentNombre.equals(objetivo)) {
                reconstruirCamino(padres, objetivo, "A*");
                return;
            }

            closedSet.add(currentNombre);
            for (Edge edge : grafo.getVecinos(currentNombre)) {
                if (closedSet.contains(edge.destino)) continue;

                double tentativeG = gScore.get(currentNombre) + edge.peso;
                if (tentativeG < gScore.getOrDefault(edge.destino, Double.MAX_VALUE)) {
                    padres.put(edge.destino, currentNombre);
                    gScore.put(edge.destino, tentativeG);
                    double fScore = tentativeG + grafo.nodos.get(edge.destino).distanciaA(nodoObjetivo);
                    openSet.add(new NodeAEstrella(edge.destino, tentativeG, fScore));
                }
            }
        }
        System.out.println("A* - No se encontró camino.");
    }

    // Método auxiliar para reconstruir la ruta
    private static void reconstruirCamino(Map<String, String> padres, String objetivo, String algoritmo) {
        List<String> camino = new ArrayList<>();
        String actual = objetivo;
        while (actual != null) {
            camino.add(0, actual);
            actual = padres.get(actual);
        }
        System.out.println(algoritmo + " - Ruta más óptima: " + camino);
    }

    // ==========================================
    // 4. MÉTODO MAIN (Ejecución de todo)
    // ==========================================
    public static void main(String[] args) {
        System.out.println("--- PRUEBA DE RECURSIVIDAD (Merge Sort) ---");
        int[] distanciasRutas = {450, 120, 800, 300, 150, 600};
        mergeSort(distanciasRutas, 0, distanciasRutas.length - 1);
        System.out.print("Rutas ordenadas: ");
        for (int d : distanciasRutas) System.out.print(d + "m ");
        System.out.println("\n");

        System.out.println("--- MODELADO DEL GRAFO DEL CAMPUS ---");
        Grafo campus = new Grafo();
        campus.agregarNodo(new Nodo("Entrada", 0, 0));
        campus.agregarNodo(new Nodo("Pabellon A", 10, 5));
        campus.agregarNodo(new Nodo("Pabellon B", 20, 2));
        campus.agregarNodo(new Nodo("Biblioteca", 15, 15));
        campus.agregarNodo(new Nodo("Laboratorio", 30, 10));

        campus.agregarArista("Entrada", "Pabellon A", 50);
        campus.agregarArista("Entrada", "Biblioteca", 120);
        campus.agregarArista("Pabellon A", "Pabellon B", 40);
        campus.agregarArista("Pabellon A", "Biblioteca", 30);
        campus.agregarArista("Biblioteca", "Laboratorio", 60);
        campus.agregarArista("Pabellon B", "Laboratorio", 80);
        System.out.println("Grafo cargado con 5 nodos y 6 aristas.\n");

        System.out.println("--- COMPARATIVA DE BÚSQUEDAS (De Entrada a Laboratorio) ---");
        dfs(campus, "Entrada", "Laboratorio");
        bfs(campus, "Entrada", "Laboratorio");
        aEstrella(campus, "Entrada", "Laboratorio");

        System.out.println("\n--- CONCLUSIÓN TÉCNICA (Para el video/informe) ---");
        System.out.println("1. DFS encuentra un camino, pero puede no ser el más corto.");
        System.out.println("2. BFS encuentra el camino con menos saltos, pero ignora las distancias (pesos).");
        System.out.println("3. A* es el más óptimo porque usa la distancia euclidiana como heurística");
        System.out.println("   para enfocarse en la meta y evitar explorar rutas innecesarias.");
    }

}
