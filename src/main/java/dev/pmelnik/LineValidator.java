package dev.pmelnik;

public final class LineValidator {

    private LineValidator() {}

    public static boolean isValidLine(String line) {
        return !line.matches(".*\\d\"\\d.*");
    }
}
