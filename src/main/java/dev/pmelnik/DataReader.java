package dev.pmelnik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class DataReader {

    public static String[][] readGzippedCsv(String filePath) throws IOException {
        try (InputStream fileStream = Files.newInputStream(Paths.get(filePath));
             InputStream gzipStream = new GZIPInputStream(fileStream);
             BufferedReader reader = new BufferedReader(new InputStreamReader(gzipStream))) {

            return reader.lines()
                    .map(line -> line.split(";"))
                    .filter(parts -> Arrays.stream(parts).allMatch(DataValidator::isValidPart))
                    .map(parts -> Arrays.stream(parts)
                            .map(DataParser::parseValue)
                            .toArray(String[]::new))
                    .filter(a -> a.length > 0)
                    .sorted((a, b) -> Integer.compare(b.length, a.length))
                    .toArray(String[][]::new);
        }
    }
}
