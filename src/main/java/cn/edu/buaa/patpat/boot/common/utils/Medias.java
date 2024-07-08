package cn.edu.buaa.patpat.boot.common.utils;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Medias {
    public void ensureParentPath(String path) throws IOException {
        ensurePath(Paths.get(path).getParent().toString());
    }

    public void ensurePath(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        Files.createDirectories(Paths.get(path));
    }

    public void save(String path, InputStreamSource file) throws IOException {
        save(Paths.get(path), file);
    }

    public void save(Path path, InputStreamSource file) throws IOException {
        FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(path));
    }
}
