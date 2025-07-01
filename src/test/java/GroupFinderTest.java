import dev.pmelnik.GroupFinder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupFinderTest {


    @Test
    void testFindGroupsWithMultipleConnections() {
        List<List<String>> lines = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("d", "b", "f"),
                Arrays.asList("a", "x", "z"),
                Arrays.asList("1", "2", "3"),
                Arrays.asList("4", "2", "6")
        );

        GroupFinder finder = new GroupFinder();
        List<List<List<String>>> groups = finder.findGroups(lines);

        assertEquals(2, groups.size());
        assertEquals(3, groups.get(0).size());
        assertEquals(2, groups.get(1).size());
    }

    @Test
    void testFindGroupsWithNoConnections() {
        List<List<String>> lines = Arrays.asList(
                Arrays.asList("1", "2", "3"),
                Arrays.asList("4", "5", "6"),
                Arrays.asList("7", "8", "9")
        );

        GroupFinder finder = new GroupFinder();
        List<List<List<String>>> groups = finder.findGroups(lines);

        assertTrue(groups.isEmpty());
    }
}
