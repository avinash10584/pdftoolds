package org.pdftools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

/**
 * Wraps useful static methods in {@link Files}. This class exists so that it
 * can be mocked in unit tests with Mockito, which can't mock static methods.
 */
@Component
public class FilesWrapper {

    /** @see Files#exists(Path, LinkOption...) */
    public boolean exists(Path path, LinkOption... options) {
        return Files.exists(path, options);
    }

    /** @see Files#readAllBytes(Path) */
    public byte[] readAllBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    /** @see Files#newInputStream(Path, OpenOption...) */
    public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        return Files.newInputStream(path, options);
    }

    /** @see Files#size(Path) */
    public long size(Path path) throws IOException {
        return Files.size(path);
    }
}
