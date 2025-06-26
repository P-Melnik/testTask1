package dev.pmelnik;

public class DataParser {

    public static String parseValue(String part) {
        return (part.isBlank() || part.equals("\"\"")) ? "" :
                part.substring(1, part.length() - 1);
    }
}
