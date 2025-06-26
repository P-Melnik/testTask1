package dev.pmelnik;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResultWriter {

    public static void writeResults(List<int[]> groups, String[][] data, String outputPath)
            throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            int multiElementGroups = (int) groups.stream().filter(g -> g.length > 1).count();
            writer.write("Групп с более чем одним элементом: " + multiElementGroups + "\n");

            int groupNum = 1;
            for (int[] group : groups) {
                writer.write("Группа " + groupNum++ + "\n");
                for (int rowIndex : group) {
                    writer.write(formatLine(data[rowIndex]) + "\n");
                }
            }
        }
    }

    private static String formatLine(String[] row) {
        return Arrays.stream(row)
                .map(value -> value.isEmpty() ? "\"\"" : "\"" + value + "\"")
                .collect(Collectors.joining(";"));
    }
}
