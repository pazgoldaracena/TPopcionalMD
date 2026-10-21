import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {

    private List<List<Dijkstra.Arista>> crearGrafo(int vertices) {
        List<List<Dijkstra.Arista>> grafo = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            grafo.add(new ArrayList<>());
        }
        return grafo;
    }

    @Test
    public void testDistanciasMinimas() {
        List<List<Dijkstra.Arista>> grafo = crearGrafo(4);
        grafo.get(0).add(new Dijkstra.Arista(1, 4));
        grafo.get(0).add(new Dijkstra.Arista(2, 2));
        grafo.get(2).add(new Dijkstra.Arista(1, 1));
        grafo.get(1).add(new Dijkstra.Arista(3, 3));
        int[] distancias = Dijkstra.dijkstra(grafo, 0);
        assertArrayEquals(new int[]{0, 3, 2, 6}, distancias);
    }

    @Test
    public void testVerticeInalcanzable() {
        List<List<Dijkstra.Arista>> grafo = crearGrafo(3);
        grafo.get(0).add(new Dijkstra.Arista(1, 5));
        int[] distancias = Dijkstra.dijkstra(grafo, 0);
        assertEquals(0, distancias[0]);
        assertEquals(5, distancias[1]);
        assertEquals(Integer.MAX_VALUE, distancias[2]);
    }

    @Test
    public void testCaminoIndirectoMasCorto() {
        List<List<Dijkstra.Arista>> grafo = crearGrafo(3);
        grafo.get(0).add(new Dijkstra.Arista(1, 10));
        grafo.get(0).add(new Dijkstra.Arista(2, 2));
        grafo.get(2).add(new Dijkstra.Arista(1, 3));
        int[] distancias = Dijkstra.dijkstra(grafo, 0);
        assertEquals(5, distancias[1]);
    }
}