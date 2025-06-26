package dev.pmelnik;

/*
Cтруктура данных, которая эффективно работает с разбиением множества
на непересекающиеся подмножества (компоненты связности).
Используется для группировки строк с одинаковыми значениями.
*/

public class DisjointSet {

    private int[] parent;

    public DisjointSet(int size) {
        parent = new int[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            int rankX = calculateRank(rootX);
            int rankY = calculateRank(rootY);

            if (rankX < rankY) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
            }
        }
    }

    private int calculateRank(int x) {
        int rank = 0;
        while (parent[x] != x) {
            x = parent[x];
            rank++;
        }
        return rank;
    }

}
