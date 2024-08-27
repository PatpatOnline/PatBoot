package cn.edu.buaa.patpat.boot.common.utils;

import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

public class Medias {
    private Medias() {}

    public static Path getParentPath(String path) {
        return getParentPath(Path.of(path));
    }

    /***
     * Get the parent path of the given path.
     */
    public static Path getParentPath(Path path) {
        return path.getParent();
    }

    public static void ensureParentPath(String path) throws IOException {
        ensureParentPath(Path.of(path));
    }

    /**
     * Ensure the parent path of the given path exists.
     */
    public static void ensureParentPath(Path path) throws IOException {
        ensurePath(path.getParent());
    }

    public static void ensurePath(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        ensurePath(Path.of(path));
    }

    /**
     * Ensure the given path exists.
     */
    public static void ensurePath(Path path) throws IOException {
        Files.createDirectories(path);
    }

    public static void ensureEmptyPath(String path) throws IOException {
        remove(path);
        ensurePath(path);
    }

    /**
     * Ensure the given path exists and is empty.
     */
    public static void ensureEmptyPath(Path path) throws IOException {
        remove(path);
        ensurePath(path);
    }

    public static void ensureEmptyParentPath(String path) throws IOException {
        ensureEmptyParentPath(Path.of(path));
    }

    /**
     * Ensure the parent path of the given path exists and is empty.
     */
    public static void ensureEmptyParentPath(Path path) throws IOException {
        ensureEmptyPath(path.getParent());
    }

    public static void save(String path, InputStreamSource file) throws IOException {
        save(Path.of(path), file);
    }

    /**
     * Save file to the given path.
     */
    public static void save(Path path, InputStreamSource file) throws IOException {
        ensureParentPath(path);
        FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(path));
    }

    public static void save(String path, String content) throws IOException {
        save(Path.of(path), content);
    }

    /**
     * Save content to the given path.
     */
    public static void save(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

    public static void remove(String path) throws IOException {
        remove(Path.of(path));
    }

    /**
     * Remove file or directory.
     */
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

    /**
     * Remove file or directory without throwing exception if failed.
     */
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

    /**
     * Check if the given path exists.
     */
    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static void copyFile(String src, String dest) throws IOException {
        copyFile(Path.of(src), Path.of(dest));
    }

    /**
     * Copy file from src to target directory.
     */
    public static void copyFile(Path src, Path dest) throws IOException {
        ensurePath(dest);
        Files.copy(src, dest.resolve(src.getFileName()));
    }

    public static void copyDirectory(String src, String dest) throws IOException {
        copyDirectory(Path.of(src), Path.of(dest));
    }

    /**
     * Copy directory from src to target directory.
     */
    public static void copyDirectory(Path src, Path dest) throws IOException {
        FileUtils.copyDirectory(src.toFile(), dest.toFile());
    }

    public static Resource loadAsResource(String path) throws IOException {
        return loadAsResource(path, false);
    }

    /**
     * Load a file as resource to return.
     */
    public static Resource loadAsResource(String path, boolean autoDelete) throws IOException {
        return loadAsResource(Path.of(path), autoDelete);
    }

    public static Resource loadAsResource(Path path) throws IOException {
        return loadAsResource(path, false);
    }

    /**
     * Load a file as resource to return.
     *
     * @param path       file path to load
     * @param autoDelete whether to delete the file after reading
     * @return resource
     * @throws IOException if failed to load resource
     */
    public static Resource loadAsResource(Path path, boolean autoDelete) throws IOException {
        Resource resource = autoDelete ? new AutoDeleteFileSystemResource(path) : new FileSystemResource(path);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Resource not found: " + path);
        }

    }

    public static Resource loadAsResource(String path, Consumer<Path> deleteAction) throws IOException {
        return loadAsResource(Path.of(path), deleteAction);
    }

    /**
     * Load a file as resource to return. And apply the delete action after reading.
     */
    public static Resource loadAsResource(Path path, Consumer<Path> deleteAction) throws IOException {
        Resource resource = deleteAction == null
                ? new FileSystemResource(path)
                : new AutoDeleteFileSystemResource(path, deleteAction);
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Resource not found: " + path);
        }
    }

    public static InputStream getInputStream(String path) throws IOException {
        return getInputStream(Path.of(path));
    }

    /**
     * Load file as input stream.
     */
    public static InputStream getInputStream(Path path) throws IOException {
        return Files.newInputStream(path);
    }

    public static OutputStream getOutputStream(String path) throws IOException {
        return getOutputStream(Path.of(path));
    }

    /**
     * Get output stream for the given path.
     */
    public static OutputStream getOutputStream(Path path) throws IOException {
        ensureParentPath(path);
        return Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static class AutoDeleteFileSystemResource extends FileSystemResource {
        private final Consumer<Path> deleteAction;

        public AutoDeleteFileSystemResource(Path path, @Nonnull Consumer<Path> deleteAction) {
            super(path);
            this.deleteAction = deleteAction;
        }

        public AutoDeleteFileSystemResource(Path path) {
            this(path, Medias::removeSilently);
        }

        @Override
        @Nonnull
        public InputStream getInputStream() throws IOException {
            return new DeleteOnCloseFileInputStream(getFile());
        }

        private final class DeleteOnCloseFileInputStream extends FileInputStream {
            private final File file;

            DeleteOnCloseFileInputStream(File file) throws FileNotFoundException {
                super(file);
                this.file = file;
            }

            @Override
            public void close() throws IOException {
                super.close();
                deleteAction.accept(file.toPath());
            }
        }
    }
}
