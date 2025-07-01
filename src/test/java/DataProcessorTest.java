import dev.pmelnik.DataProcessor;
import dev.pmelnik.GroupAnalysisResult;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataProcessorTest {

    @TempDir
    Path tempDir;

    @Test
    void testProcessFileWithValidData() throws IOException {
        // Создаем настоящий gzip-файл
        File testFile = tempDir.resolve("test.csv.gz").toFile();
        createTestGzipFile(testFile, List.of(
                "1;2;3",
                "4;5;6",
                "1;2;3",
                "7;8;9",
                "1;5;9"  // должна объединиться с первой группой через "5"
        ));

        DataProcessor processor = new DataProcessor();
        GroupAnalysisResult result = processor.processFile(testFile);

        assertEquals(1, result.getMultiElementGroupsCount());
        assertTrue(new File("output.txt").exists());

        new File("output.txt").delete();
    }

    @Test
    void testProcessFileWithNoGroups() throws IOException {
        File testFile = tempDir.resolve("no_groups.csv.gz").toFile();
        createTestGzipFile(testFile, List.of(
                "1;2;3",
                "4;5;6",
                "7;8;9"
        ));

        DataProcessor processor = new DataProcessor();
        GroupAnalysisResult result = processor.processFile(testFile);

        assertEquals(0, result.getMultiElementGroupsCount());
        new File("output.txt").delete();
    }

    private void createTestGzipFile(File file, List<String> lines) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(fos);
             OutputStreamWriter osw = new OutputStreamWriter(gzos);
             BufferedWriter writer = new BufferedWriter(osw)) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
