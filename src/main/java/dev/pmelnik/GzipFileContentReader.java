package dev.pmelnik;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class GzipFileContentReader implements FileContentReader {
    private final File archiveFile;

    public GzipFileContentReader(File archiveFile) {
        this.archiveFile = archiveFile;
    }

    @Override
    public FileReadResult readContent() throws IOException {
        Set<String> uniqueLines = new LinkedHashSet<>();
        int total = 0;
        int valid = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new GzipCompressorInputStream(new FileInputStream(archiveFile))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                total++;
                if (LineValidator.isValidLine(line)) {
                    uniqueLines.add(line);
                    valid++;
                }
            }
        }

        return new FileReadResult(total, new ArrayList<>(uniqueLines));
    }
}
