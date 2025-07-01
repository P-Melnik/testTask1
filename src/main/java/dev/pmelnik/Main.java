package dev.pmelnik;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            validateArguments(args);
            File inputFile = getInputFile(args[0]);

            Instant start = Instant.now();
            GroupAnalysisResult result = new DataProcessor().processFile(inputFile);

            printResults(result, start);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }


    private static void validateArguments(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No file path provided");
        }
    }

    private static File getInputFile(String filePath) {
        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        return inputFile;
    }

    private static void printResults(GroupAnalysisResult result, Instant startTime) {
        result.printStatistics();
        long durationMs = Duration.between(startTime, Instant.now()).toMillis();
        System.out.printf("Execution time: %d ms%n", durationMs);
    }
}
