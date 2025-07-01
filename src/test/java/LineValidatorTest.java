import dev.pmelnik.LineValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineValidatorTest {

    @Test
    void testIsValidLine() {
        assertTrue(LineValidator.isValidLine("abc;def;123"));
        assertTrue(LineValidator.isValidLine("1;2;3"));
        assertTrue(LineValidator.isValidLine("\"1\";\"2\";\"3\""));

        assertFalse(LineValidator.isValidLine("abc;1\"23;def"));
        assertFalse(LineValidator.isValidLine("1\"2;3;4"));
        assertFalse(LineValidator.isValidLine("1;2;3\"4"));
    }
}
