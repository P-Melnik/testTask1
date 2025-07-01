package dev.pmelnik;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataProcessor {

    private final CsvFileReader fileReader;
    private final GroupFinder groupFinder;
    private final GroupWriter groupWriter;

    public DataProcessor() {
        this.fileReader = new CsvFileReader();
        this.groupFinder = new GroupFinder();
        this.groupWriter = new GroupWriter();
    }

    public GroupAnalysisResult processFile(File inputFile) throws IOException {
        List<List<String>> validLines = fileReader.readValidLines(inputFile);
        List<List<List<String>>> groups = groupFinder.findGroups(validLines);
        groupWriter.writeGroupsToFile(groups, new File("output.txt"));
        return new GroupAnalysisResult(groups);
    }
}
