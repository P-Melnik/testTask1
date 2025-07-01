package dev.pmelnik;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class GroupWriter {

    public void writeGroupsToFile(List<List<List<String>>> groups, File outFile) {
        try (PrintWriter pw = new PrintWriter(outFile)) {
            int groupNumber = 1;
            for (List<List<String>> group : groups) {
                pw.println("Group " + groupNumber++);
                group.forEach(row -> pw.println(String.join(";", row)));
                pw.println();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write groups to file", e);
        }
    }
}
