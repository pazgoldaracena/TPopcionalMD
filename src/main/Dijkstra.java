import java.util.*;

public class Dijkstra {

    // Ruta que sale de un vértice, guardando a dónde llega y cuánto cuesta recorrerla
    public static class Arista {
        int destino;
        int peso;

        public Arista(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // Anotación que informa una manera de llegar a un vértice determinado y la distancia correspondiente
    // Son lo que insertamos en la PQ, ya que queremos que dicha distancia sea la menor
    // Se implementa Comparable para ordenar los Nodos por distancia y siempre sacar primero el de menor distancia
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

    public static int[] dijkstra(List<List<Arista>> grafo, int origen) {
        int[] dist = new int[grafo.size()]; // Array de distancias del tamaño de la cantidad de vertices en el grafo
        Arrays.fill(dist, Integer.MAX_VALUE); // Por default, las setteamos como "tendiendo a infinito"
        dist[origen] = 0; // Menos el origen, la cual tiene distancia 0

        PriorityQueue<Nodo> cola = new PriorityQueue<>();
        cola.add(new Nodo(origen, 0)); // Agregamos el origen a la PQ

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll(); // Saco la anotación con menor distancia
            int u = actual.vertice; // Me guardo el valor del vértice

            // Si la distancia de la anotación es mayor (peor) a la que tengo guardada en su posición correspondiente del Array
            // La ignoro y continuo con el próximo nodo de la cola
            if (actual.distancia > dist[u]) continue;

            // Recorro las Aristas correspondientes al Nodo con vértice u (en el índice u)
            for (Arista a : grafo.get(u)) {
                int nueva = dist[u] + a.peso; // Me guardo la distancia que ya tenía guardada + el peso correspondiente a la Arista actual
                if (nueva < dist[a.destino]) { // Si esa nueva distancia calculada es mejor a la distancia en el índice del destino
                    dist[a.destino] = nueva; // Actualizo el índice para que corresponda al mejor camino con la menor distancia
                    cola.add(new Nodo(a.destino, nueva)); // Agrego un nuevo Nodo a la PQ que me índique que encontré una nueva distancia mínima
                }
            }
        }

        return dist; // Devuelve la distancia minima desde origen a cada vertice.
    }
}