package dev.pmelnik;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Pls use: java -jar <name>.jar filePath.txt.gz");
            return;
        }

        try {
            long startTime = System.currentTimeMillis();

            String[][] data = DataReader.readGzippedCsv(args[0]);

            List<int[]> groups = DataGrouper.groupData(data);

            ResultWriter.writeResults(groups, data, "output.txt");

            printStatistics(startTime, groups);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printStatistics(long startTime, List<int[]> groups) {
        long multiElementGroups = groups.stream().filter(g -> g.length > 1).count();
        double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;

        System.out.println("Groups with more then one element: " + multiElementGroups);
        System.out.printf("Execution time: %.3f sec.%n", elapsedTime);
    }

}
