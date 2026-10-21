import java.util.*;

public class Kruskal {

    public static class Edge implements Comparable<Edge> {
        int origin;
        int next;
        int weight;

        public Edge (int origin, int next, int weight) {
            this.origin = origin;
            this.next = next;
            this.weight = weight;
        }

        @Override
        public int compareTo (Edge a) {
            return Integer.compare(this.weight, a.weight);
        }
    }
    public static class UnionFind {

        private final int[] parent; //represents sets
        private final int[] range; //optimize the unions, allows to keep trees shallow

        //at the beginning each vertex starts belonging to its own set -> each vertex starts being its own parent
        public UnionFind (int n) {
            parent = new int[n]; // array with n positions for each vertex
            range = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        public int find (int x) { //finds the root of each set -> what is the representative of the set to which vertex x belongs?
            if (parent[x] != x) {
                parent[x] = find(parent[x]); //optimization -> PATH COMPRESSION -> updates vertex's parent
            }
            return parent[x];
        }

        public boolean union (int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) { //there's no union -> already connected
                return false;
            }
            if (range[rootX] < range[rootY]) {
                parent[rootX] = rootY;
            } else if (range[rootX] > range[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                range[rootX]++;
            }
            return true; //vertices in different sets -> ended up connected
        }
    }
    public static List<Edge> kruskal(int vertices, List<Edge> edges) {
        List<Edge> ordered = new ArrayList<>(edges);
        Collections.sort(ordered);
        UnionFind uf = new UnionFind(vertices);
        List<Edge> mst = new ArrayList<>();
        for (Edge edge : ordered) {
            if (uf.union(edge.origin, edge.next)) { //checks different sets
                mst.add(edge);
            }
            if (mst.size() == vertices - 1) { //tree property
                break;
            }
        }
        return mst;
    }
}
