package dev.pmelnik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataGrouper {

    public static List<int[]> groupData(String[][] values) {
        DisjointSet disjointSet = new DisjointSet(values.length);

        findAndUnionMatches(values, disjointSet);

        return buildGroups(disjointSet, values.length);
    }

    /**
     * Находит совпадения значений в столбцах и объединяет строки
     */
    private static void findAndUnionMatches(String[][] values, DisjointSet disjointSet) {
        for (int col = 0; col < values[0].length; col++) {
            Map<String, Integer> valueToRowIndex = new HashMap<>();

            for (int row = 0; row < values.length && col < values[row].length; row++) {
                String currentValue = values[row][col];

                if (currentValue == null || currentValue.isEmpty()) {
                    continue;
                }

                if (valueToRowIndex.containsKey(currentValue)) {
                    int matchedRow = valueToRowIndex.get(currentValue);
                    disjointSet.union(matchedRow, row);
                } else {
                    valueToRowIndex.put(currentValue, row);
                }
            }
        }
    }

    /**
     * Строит группы на основе установленных связей
     */
    private static List<int[]> buildGroups(DisjointSet disjointSet, int totalRows) {
        Map<Integer, List<Integer>> groupsMap = new HashMap<>();

        for (int row = 0; row < totalRows; row++) {
            int root = disjointSet.find(row);
            groupsMap.computeIfAbsent(root, k -> new ArrayList<>()).add(row);
        }

        List<int[]> result = groupsMap.values()
                .stream()
                .map(list -> list.stream().mapToInt(i -> i).toArray())
                .collect(Collectors.toList());

        result.sort((a, b) -> Integer.compare(b.length, a.length));

        return result;
    }
}
