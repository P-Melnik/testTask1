package dev.pmelnik;

import java.io.IOException;

public interface FileContentReader {
    FileReadResult readContent() throws IOException;
}
