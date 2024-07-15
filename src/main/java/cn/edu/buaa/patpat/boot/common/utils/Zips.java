package cn.edu.buaa.patpat.boot.common.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Zips {
    private Zips() {}

    /**
     * Unzip a zip file to a destination directory without creating a new directory.
     *
     * @param zipFile The path to the zip file.
     * @param destDir The destination directory.
     * @throws IOException If an I/O error occurs.
     */
    public static void unzip(String zipFile, String destDir) throws IOException {
        try (var archive = new ZipFile(zipFile)) {
            archive.extractAll(destDir);
        }
    }

    public static void unzip(Path zipFile, Path destDir) throws IOException {
        unzip(zipFile.toString(), destDir.toString());
    }

    /**
     * Zip a directory to a .zip archive.
     *
     * @param sourceDir     The path to the directory to zip.
     * @param zipFilePath   The path to the .zip archive.
     * @param includeParent Whether to include the parent directory in the archive.
     * @throws IOException If an I/O error occurs.
     */
    public static void zip(Path sourceDir, Path zipFilePath, boolean includeParent) throws IOException {
        var params = new ZipParameters();
        params.setIncludeRootFolder(includeParent);
        try (var archive = new ZipFile(zipFilePath.toFile())) {
            archive.addFolder(sourceDir.toFile(), params);
        } catch (ZipException e) {
            throw new IOException(e);
        }
    }

    public static void zip(String sourceDir, String zipFilePath, boolean includeParent) throws IOException {
        zip(Path.of(sourceDir), Path.of(zipFilePath), includeParent);
    }
}
