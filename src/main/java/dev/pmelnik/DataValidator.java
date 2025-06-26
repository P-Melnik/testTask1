package dev.pmelnik;

public class DataValidator {

    public static boolean isValidPart(String part) {
        return part.isBlank() || part.equals("\"\"") ||
                (part.startsWith("\"") && part.endsWith("\""));
    }
}
