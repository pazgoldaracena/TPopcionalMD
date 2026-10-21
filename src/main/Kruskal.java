import java.util.*;

public class Kruskal {

    public static class Arista implements Comparable<Arista> {
        int origen;
        int destino;
        int peso;

        public Arista(int origen, int destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }

        @Override
        public int compareTo(Arista otra) {
            return Integer.compare(this.peso, otra.peso);
        }
    }
    public static class UnionFind {

        private final int[] padre;
        private final int[] rango;

        public UnionFind(int n) {
            padre = new int[n];
            rango = new int[n];

            for (int i = 0; i < n; i++) {
                padre[i] = i;
            }
        }

        public int find(int x) {
            if (padre[x] != x) {
                padre[x] = find(padre[x]);
            }
            return padre[x];
        }

        public boolean union(int x, int y) {
            int raizX = find(x);
            int raizY = find(y);

            if (raizX == raizY) {
                return false;
            }

            if (rango[raizX] < rango[raizY]) {
                padre[raizX] = raizY;
            } else if (rango[raizX] > rango[raizY]) {
                padre[raizY] = raizX;
            } else {
                padre[raizY] = raizX;
                rango[raizX]++;
            }

            return true;
        }
    }
    public static List<Arista> kruskal(int vertices, List<Arista> aristas) {

        List<Arista> ordenadas = new ArrayList<>(aristas);
        Collections.sort(ordenadas);

        UnionFind uf = new UnionFind(vertices);

        List<Arista> mst = new ArrayList<>();

        for (Arista arista : ordenadas) {

            if (uf.union(arista.origen, arista.destino)) {
                mst.add(arista);
            }

            if (mst.size() == vertices - 1) {
                break;
            }
        }
        return mst;
    }
}
