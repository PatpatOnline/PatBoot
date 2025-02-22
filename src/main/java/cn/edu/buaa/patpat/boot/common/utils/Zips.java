/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.common.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Predicate;

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
        if (!sourceDir.toFile().exists()) {
            throw new IOException("Source directory does not exist.");
        }

        Medias.ensureParentPath(zipFilePath.toString());

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

    /**
     * Zip files under a directory to a .zip archive with a filter.
     * Note that it only searches for files under the directory, not recursively.
     *
     * @param sourceDir     The path to the directory to zip.
     * @param zipFilePath   The path to the .zip archive.
     * @param includeParent Whether to include the parent directory in the archive.
     * @param filter        The filter to apply to the files.
     * @throws IOException If an I/O error occurs.
     */
    public static void zip(Path sourceDir, Path zipFilePath, Predicate<String> filter) throws IOException {
        if (!sourceDir.toFile().exists()) {
            throw new IOException("Source directory does not exist.");
        }
        var files = sourceDir.toFile().listFiles();
        if (files == null) {
            throw new IOException("Failed to list files in the source directory.");
        }

        var params = new ZipParameters();
        params.setIncludeRootFolder(true);
        try (var archive = new ZipFile(zipFilePath.toFile())) {
            for (var file : files) {
                if (filter.test(file.getName())) {
                    archive.addFolder(file, params);
                }
            }
        } catch (ZipException e) {
            throw new IOException(e);
        }
    }

    public static void zip(String sourceDir, String zipFilePath, Predicate<String> filter) throws IOException {
        zip(Path.of(sourceDir), Path.of(zipFilePath), filter);
    }

    /**
     * Add files to an existing zip archive.
     * Shouldn't pass directories to this method.
     *
     * @param zipFile The path to the zip archive.
     * @param files   The paths to the files to add.
     * @throws IOException If an I/O error occurs.
     */
    public static void update(Path zipFile, Path... files) throws IOException {
        try (var archive = new ZipFile(zipFile.toFile())) {
            archive.addFiles(Arrays.stream(files).map(Path::toFile).toList());
        } catch (ZipException e) {
            throw new IOException(e);
        }

    }

    public static void update(String zipFile, String... files) throws IOException {
        update(Path.of(zipFile), Arrays.stream(files).map(Path::of).toArray(Path[]::new));
    }
}
