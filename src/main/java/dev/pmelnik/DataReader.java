package dev.pmelnik;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class DataReader {

    /**
     * Читает файл (распаковывает .gz или 7z при необходимости)
     * @param filePath путь к файлу
     */
    public static String[][] readFile(String filePath) throws IOException {
        try (BufferedReader reader = createReader(filePath)) {
            return parseLines(reader.lines());
        }
    }

    /**
     * Создает BufferedReader с автоматическим распаковыванием .gz или 7z
     */
    private static BufferedReader createReader(String filePath) throws IOException {
        InputStream fileStream = Files.newInputStream(Paths.get(filePath));

        if (filePath.toLowerCase().endsWith(".gz")) {
            return new BufferedReader(
                    new InputStreamReader(
                            new GZIPInputStream(fileStream), StandardCharsets.UTF_8));
        }
        else if (filePath.toLowerCase().endsWith(".7z")) {
            SevenZFile sevenZFile = new SevenZFile(Paths.get(filePath).toFile());
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName().toLowerCase();
                    if (entryName.endsWith(".csv") || entryName.endsWith(".txt")) {
                        File tempFile = File.createTempFile("7zentry", ".csv");
                        tempFile.deleteOnExit();

                        try (OutputStream out = new FileOutputStream(tempFile)) {
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = sevenZFile.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }
                        sevenZFile.close();

                        return Files.newBufferedReader(tempFile.toPath(), StandardCharsets.UTF_8);
                    }
                }
            }
            sevenZFile.close();
            throw new IOException("Не найден CSV или текстовый файл в 7z архиве");
        }
        return new BufferedReader(
                new InputStreamReader(fileStream, StandardCharsets.UTF_8));
    }

    /**
     * Парсит строки в 2D массив
     */
    private static String[][] parseLines(Stream<String> lines) {
        return lines
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
