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

        private final int[] parent;
        private final int[] range;

        public UnionFind(int n) {
            parent = new int[n];
            range = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find (int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union (int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
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
            return true;
        }
    }
    public static List<Edge> kruskal(int vertices, List<Edge> edges) {
        List<Edge> ordered = new ArrayList<>(edges);
        Collections.sort(ordered);
        UnionFind uf = new UnionFind(vertices);
        List<Edge> mst = new ArrayList<>();
        for (Edge edge : ordered) {
            if (uf.union(edge.origin, edge.next)) {
                mst.add(edge);
            }
            if (mst.size() == vertices - 1) {
                break;
            }
        }
        return mst;
    }
}
