package dev.pmelnik;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SevenZFileContentReader implements FileContentReader {
    private final File archiveFile;

    public SevenZFileContentReader(File archiveFile) {
        this.archiveFile = archiveFile;
    }

    @Override
    public FileReadResult readContent() throws IOException {
        Set<String> uniqueLines = new LinkedHashSet<>();
        int total = 0;
        int valid = 0;

        try (SevenZFile sevenZFile = new SevenZFile(archiveFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (entry.getName().toLowerCase().endsWith(".csv")) {
                    InputStream inputStream = sevenZFile.getInputStream(entry);
                    processInputStream(inputStream, uniqueLines, new int[]{total}, new int[]{valid});
                }
            }
        }

        return new FileReadResult(total, new ArrayList<>(uniqueLines));
    }

    private void processInputStream(InputStream inputStream, Set<String> uniqueLines, int[] total, int[] valid) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                total[0]++;
                if (LineValidator.isValidLine(line)) {
                    uniqueLines.add(line);
                    valid[0]++;
                }
            }
        }
    }
}
