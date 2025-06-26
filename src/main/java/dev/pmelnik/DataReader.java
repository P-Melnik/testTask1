package dev.pmelnik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class DataReader {

    /**
     * Читает файл (распаковывает .gz при необходимости)
     * @param filePath путь к файлу (.txt или .gz)
     */
    public static String[][] readFile(String filePath) throws IOException {
        try (BufferedReader reader = createReader(filePath)) {
            return parseLines(reader.lines());
        }
    }

    /**
     * Создает BufferedReader с автоматическим распаковыванием .gz
     */
    private static BufferedReader createReader(String filePath) throws IOException {
        InputStream fileStream = Files.newInputStream(Paths.get(filePath));

        // Если файл .gz - добавляем GZIP распаковку
        if (filePath.toLowerCase().endsWith(".gz")) {
            return new BufferedReader(
                    new InputStreamReader(
                            new GZIPInputStream(fileStream)));
        }

        // Для обычных файлов
        return new BufferedReader(
                new InputStreamReader(fileStream)
        );
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
