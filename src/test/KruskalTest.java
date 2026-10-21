import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class KruskalTest {

    @Test
    public void testMSTSimple() {
        List<Kruskal.Edge> aristas = new ArrayList<>();
        aristas.add(new Kruskal.Edge(0, 1, 4));
        aristas.add(new Kruskal.Edge(0, 2, 2));
        aristas.add(new Kruskal.Edge(1, 3, 5));
        aristas.add(new Kruskal.Edge(2, 3, 3));
        List<Kruskal.Edge> mst = Kruskal.kruskal(4, aristas);
        int pesoTotal = 0;
        for (Kruskal.Edge arista : mst) {
            pesoTotal += arista.weight;
        }
        assertEquals(3, mst.size());
        assertEquals(9, pesoTotal);
    }

    @Test
    public void testEvitaCiclos() {
        List<Kruskal.Edge> aristas = new ArrayList<>();
        aristas.add(new Kruskal.Edge(0, 1, 2));
        aristas.add(new Kruskal.Edge(1, 2, 3));
        aristas.add(new Kruskal.Edge(0, 2, 4));
        List<Kruskal.Edge> mst = Kruskal.kruskal(3, aristas);
        int pesoTotal = 0;
        for (Kruskal.Edge arista : mst) {
            pesoTotal += arista.weight;
        }
        assertEquals(2, mst.size());
        assertEquals(5, pesoTotal);
    }

    @Test
    public void testGrafoDesconectado() {
        List<Kruskal.Edge> aristas = new ArrayList<>();
        aristas.add(new Kruskal.Edge(0, 1, 2));
        aristas.add(new Kruskal.Edge(2, 3, 3));
        List<Kruskal.Edge> mst = Kruskal.kruskal(4, aristas);
        assertEquals(2, mst.size());
    }
}
