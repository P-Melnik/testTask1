package dev.pmelnik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupFinder {

    public List<List<List<String>>> findGroups(List<List<String>> lines) {
        UnionFind uf = new UnionFind(lines.size());
        buildConnections(lines, uf);

        Map<Integer, List<List<String>>> groups = groupLinesByRoot(lines, uf);

        return groups.values().stream()
                .filter(group -> group.size() >= 2)
                .sorted((a, b) -> b.size() - a.size())
                .collect(Collectors.toList());
    }

    private void buildConnections(List<List<String>> lines, UnionFind uf) {
        Map<String, Integer> valueColToFirstRow = new HashMap<>();

        for (int row = 0; row < lines.size(); row++) {
            List<String> line = lines.get(row);
            for (int col = 0; col < line.size(); col++) {
                String val = line.get(col).trim();
                if (!val.isEmpty()) {
                    String key = val + "#" + col;
                    int finalRow = row;
                    valueColToFirstRow.compute(key, (k, firstRow) -> {
                        if (firstRow != null) {
                            uf.union(firstRow, finalRow);
                        }
                        return firstRow == null ? finalRow : firstRow;
                    });
                }
            }
        }
    }

    private Map<Integer, List<List<String>>> groupLinesByRoot(List<List<String>> lines, UnionFind uf) {
        Map<Integer, List<List<String>>> groups = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            int root = uf.find(i);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(lines.get(i));
        }
        return groups;
    }
}
