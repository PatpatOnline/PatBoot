/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.bucket.services;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final PathService pathService;

    /**
     * Save public shared content.
     *
     * @see BucketService#saveToPublic(String, String, InputStreamSource)
     */
    public String saveToPublic(String originalFilename, InputStreamSource source) throws IOException {
        return saveToPublic(null, originalFilename, source);
    }

    /**
     * Save a file to the public path.
     *
     * @param tag              The tag of the content. If is null or empty, will
     * @param originalFilename The original filename of the file.
     * @param source           The source of the file.
     * @return The record of the file to be saved in the database.
     * @throws IOException If an I/O error occurs.
     */
    public String saveToPublic(String tag, String originalFilename, InputStreamSource source) throws IOException {
        return saveFile(tag, originalFilename, source, true);
    }

    /**
     * Save private shared content.
     *
     * @see BucketService#saveToPrivate(String, String, InputStreamSource)
     */
    public String saveToPrivate(String originalFilename, InputStreamSource source) throws IOException {
        return saveToPrivate(null, originalFilename, source);
    }

    /**
     * Save a file to the private path.
     *
     * @param tag              The tag of the content. If is null or empty, will
     * @param originalFilename The original filename of the file.
     * @param source           The source of the file.
     * @return The record of the file to be saved in the database.
     * @throws IOException If an I/O error occurs.
     */
    public String saveToPrivate(String tag, String originalFilename, InputStreamSource source) throws IOException {
        return saveFile(tag, originalFilename, source, false);
    }

    /**
     * The implementation of saving a file to the path.
     *
     * @param tag              The tag of the content.
     * @param originalFilename The original filename of the file.
     * @param source           The source of the file.
     * @param isPublic         Whether the file is public.
     * @return The record of the file to be saved in the database.
     * @throws IOException If an I/O error occurs.
     */
    private String saveFile(String tag, String originalFilename, InputStreamSource source, boolean isPublic) throws IOException {
        String filename = generateFilename(originalFilename);
        String record = pathService.toRecord(tag, filename);
        String path;
        if (isPublic) {
            path = pathService.recordToPublicPath(record);
        } else {
            path = pathService.recordToPrivatePath(record);
        }
        // ensure the path exists
        Medias.ensureParentPath(path);
        Medias.save(path, source);

        return record;
    }

    /**
     * Generate a random filename.
     *
     * @param originalFilename The original filename.
     * @return The generated filename.
     */
    private String generateFilename(String originalFilename) {
        String ext = FilenameUtils.getExtension(originalFilename);
        String filename = RandomStringUtils.randomAlphanumeric(16);
        return filename + "." + ext;
    }
}
