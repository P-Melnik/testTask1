package dev.pmelnik;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvFileReader {

    public List<List<String>> readValidLines(File archiveFile) throws IOException {
        FileContentReader contentReader = createContentReader(archiveFile);
        FileReadResult readResult = contentReader.readContent();

        return readResult.validLines().stream()
                .map(this::parseAndCleanCsvLine)
                .collect(Collectors.toList());
    }

    private FileContentReader createContentReader(File archiveFile) {
        String fileName = archiveFile.getName().toLowerCase();

        if (fileName.endsWith(".7z")) {
            return new SevenZFileContentReader(archiveFile);
        } else if (fileName.endsWith(".gz")) {
            return new GzipFileContentReader(archiveFile);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName + ". Expected .7z or .gz");
        }
    }

    private List<String> parseAndCleanCsvLine(String rawLine) {
        return Arrays.stream(rawLine.split(";", -1))
                .map(part -> part.replace("\"", "").trim())
                .collect(Collectors.toList());
    }
}
