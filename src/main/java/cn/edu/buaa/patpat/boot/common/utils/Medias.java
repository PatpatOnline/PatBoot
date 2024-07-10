package cn.edu.buaa.patpat.boot.common.utils;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class Medias {
    public void ensureParentPath(String path) throws IOException {
        ensurePath(Path.of(path).getParent().toString());
    }

    public void ensurePath(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        Files.createDirectories(Path.of(path));
    }

    public void save(String path, InputStreamSource file) throws IOException {
        save(Path.of(path), file);
    }

    public void save(Path path, InputStreamSource file) throws IOException {
        FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(path));
    }

    public void remove(String path) throws IOException {
        remove(Path.of(path));
    }

    public void remove(Path path) throws IOException {
        Files.deleteIfExists(path);
    }
}
