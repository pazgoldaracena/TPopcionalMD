import java.util.*;

public class Dijkstra {

    public static class Arista {
        int destino;
        int peso;

        public Arista(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // Elemento de la cola de prioridad: se ordena por distancia
    public static class Nodo implements Comparable<Nodo> {
        int vertice;
        int distancia;

        public Nodo(int vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(Nodo otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }
    }

    // Devuelve la distancia minima desde origen a cada vertice.
    // Complejidad: O((V + E) log V)
    public static int[] dijkstra(List<List<Arista>> grafo, int origen) {
        int[] dist = new int[grafo.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[origen] = 0;

        PriorityQueue<Nodo> cola = new PriorityQueue<>();
        cola.add(new Nodo(origen, 0));

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            int u = actual.vertice;

            // Entrada desactualizada: ya se encontro un camino mejor a u
            if (actual.distancia > dist[u]) continue;

            for (Arista a : grafo.get(u)) {
                int nueva = dist[u] + a.peso;
                if (nueva < dist[a.destino]) {
                    dist[a.destino] = nueva;
                    cola.add(new Nodo(a.destino, nueva));
                }
            }
        }

        return dist;
    }
}