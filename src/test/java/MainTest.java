import dev.pmelnik.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    private static final String TEST_FILE_INPUT_1 = "test1.txt";
    private static final String TEST_FILE_INPUT_2 = "test2.txt";
    private static final String TEST_FILE_INPUT_3 = "test3.txt";
    private static final String TEST_FILE_OUTPUT = "output.txt";

    @TempDir
    Path tempDir;

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get("output.txt"));
    }

    @Test
    void testGroupingWithCommonValues() throws IOException {
        Path inputFile = tempDir.resolve(TEST_FILE_INPUT_1);
        Files.write(inputFile, List.of(
                "\"1\";\"2\";\"3\"",
                "\"1\";\"5\";\"\"",
                "\"\";\"2\";\"4\""
        ));

        Main.main(new String[]{inputFile.toString()});

        Path outputFile = Paths.get(TEST_FILE_OUTPUT);
        List<String> outputLines = Files.readAllLines(outputFile);

        assertTrue(outputLines.get(0).contains("Groups with more than one element: 1"));
        assertTrue(outputLines.stream().anyMatch(line -> line.contains("Group 1")));
    }

    @Test
    void testNoGroupsWhenAllDifferent() throws IOException {
        Path inputFile = tempDir.resolve(TEST_FILE_INPUT_2);
        Files.write(inputFile, List.of(
                "\"1\";\"2\"",
                "\"3\";\"4\"",
                "\"5\";\"6\""
        ));

        Main.main(new String[]{inputFile.toString()});

        List<String> outputLines = Files.readAllLines(Paths.get(TEST_FILE_OUTPUT));
        assertTrue(outputLines.get(0).contains("Groups with more than one element: 0"));
    }

    @Test
    void testBasicGrouping() throws Exception {
        Path inputFile = tempDir.resolve(TEST_FILE_INPUT_3);
        Files.write(inputFile, List.of(
                "\"A\";\"B\"",
                "\"A\";\"C\"",

                "\"X\";\"Y\"",
                "\"X\";\"Z\"",

                "\"UNIQUE1\";\"VAL1\"",
                "\"UNIQUE2\";\"VAL2\""
        ));

        Main.main(new String[]{inputFile.toString()});

        List<String> output = Files.readAllLines(Paths.get(TEST_FILE_OUTPUT));

        assertTrue(output.get(0).contains("Groups with more than one element: 2"));

        long groupCount = output.stream()
                .filter(line -> line.startsWith("Group "))
                .count();
        assertEquals(4, groupCount); // 2 группы по 2 + 2 одиночных

        long dataLines = output.stream()
                .filter(line -> !line.startsWith("Group") &&
                        !line.contains("Groups with more than one element:"))
                .count();
        assertEquals(6, dataLines);
    }

}
