package cn.edu.buaa.patpat.boot.common.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Medias {
    private Medias() {}

    public static void ensureParentPath(String path) throws IOException {
        ensureParentPath(Path.of(path));
    }

    public static void ensureParentPath(Path path) throws IOException {
        ensurePath(path.getParent());
    }

    public static void ensurePath(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        ensurePath(Path.of(path));
    }

    public static void ensurePath(Path path) throws IOException {
        Files.createDirectories(path);
    }

    public static void ensureEmptyPath(String path) throws IOException {
        remove(path);
        ensurePath(path);
    }

    public static void ensureEmptyPath(Path path) throws IOException {
        remove(path);
        ensurePath(path);
    }

    public static void save(String path, InputStreamSource file) throws IOException {
        save(Path.of(path), file);
    }

    public static void save(Path path, InputStreamSource file) throws IOException {
        ensureParentPath(path);
        FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(path));
    }

    public static void save(String path, String content) throws IOException {
        save(Path.of(path), content);
    }

    public static void save(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

    public static void remove(String path) throws IOException {
        remove(Path.of(path));
    }

    public static void remove(Path path) throws IOException {
        // delete file or directory recursively
        if (Files.isDirectory(path)) {
            FileUtils.deleteDirectory(path.toFile());
        } else {
            Files.deleteIfExists(path);
        }
    }

    public static void removeSilently(String path) {
        removeSilently(Path.of(path));
    }

    public static void removeSilently(Path path) {
        try {
            remove(path);
        } catch (IOException e) {
            // ignore
        }
    }

    public static boolean exists(String path) {
        return exists(Path.of(path));
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }
}
