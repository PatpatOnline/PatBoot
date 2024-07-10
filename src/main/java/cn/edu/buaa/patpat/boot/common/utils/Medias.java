package cn.edu.buaa.patpat.boot.common.utils;

import org.springframework.core.io.InputStreamSource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Medias {
    private Medias() {}

    public static void ensureParentPath(String path) throws IOException {
        ensurePath(Path.of(path).getParent().toString());
    }

    public static void ensurePath(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        Files.createDirectories(Path.of(path));
    }

    public static void save(String path, InputStreamSource file) throws IOException {
        save(Path.of(path), file);
    }

    public static void save(Path path, InputStreamSource file) throws IOException {
        FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(path));
    }

    public static void remove(String path) throws IOException {
        remove(Path.of(path));
    }

    public static void remove(Path path) throws IOException {
        Files.deleteIfExists(path);
    }
}
