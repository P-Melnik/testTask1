package dev.pmelnik;

import java.util.List;

public record FileReadResult(int totalLines, List<String> validLines) {

}
