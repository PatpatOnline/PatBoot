/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.bucket.api;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.modules.bucket.exceptions.MalformedPathException;
import cn.edu.buaa.patpat.boot.modules.bucket.services.PathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketApi {
    private final PathService pathService;

    /**
     * Convert the URL to the path.
     * This only converts the URL to a public content path.
     *
     * @param url The URL of the content.
     * @return The path of the content.
     */
    public String urlToPath(String url) {
        try {
            String record = pathService.url2Record(url);
            return pathService.recordToPublicPath(record);
        } catch (MalformedPathException e) {
            return null;
        }
    }

    /**
     * Convert the URL to the record.
     *
     * @param url The URL of the content.
     * @return The record of the content.
     */
    public String urlToRecord(String url) {
        try {
            return pathService.url2Record(url);
        } catch (MalformedPathException e) {
            return null;
        }
    }

    /**
     * @see PathService#recordToPublicPath(String)
     */
    public String recordToPublicPath(String record) {
        return pathService.recordToPublicPath(record);
    }

    /**
     * @see PathService#recordToPrivatePath(String)
     */
    public String recordToPrivatePath(String record) {
        return pathService.recordToPrivatePath(record);
    }

    /**
     * @see PathService#recordToUrl(String)
     */
    public String recordToUrl(String record) {
        return pathService.recordToUrl(record);
    }

    /**
     * Get a random directory under temp directory.
     */
    public String getRandomTempPath() {
        return recordToPrivatePath(toRandomRecord(Globals.TEMP_TAG, ""));
    }

    /**
     * Get a temp directory with the given filename under temp directory.
     */
    public String getTempPath(String filename) {
        return recordToPrivatePath(toRecord(Globals.TEMP_TAG, filename));
    }

    /**
     * Get a random temp file with the given extension.
     */
    public String getRandomTempFile(String extension) {
        return getRandomTempPath() + "." + extension;
    }

    /**
     * Get a temp file with the given filename.
     */
    public String getTempFile(String filename) {
        return recordToPrivatePath(toRecord(Globals.TEMP_TAG, filename));
    }


    /**
     * @see PathService#toRecord(String, String)
     */
    public String toRandomRecord(String tag, String filename) {
        return pathService.toRecord(tag, filename);
    }

    /**
     * @see PathService#toRecord(String)
     */
    public String toRandomRecord(String filename) {
        return pathService.toRecord(filename);
    }

    /**
     * @see PathService#toRecord(String, String, boolean)
     */
    public String toRecord(String tag, String filename) {
        return pathService.toRecord(tag, filename, false);
    }

    public String toRecord(String tag, String... args) {
        return pathService.toRecord(tag, String.join("/", args), false);
    }

    /**
     * @see PathService#toRecord(String)
     */
    public String toRecord(String filename) {
        return pathService.toRecord(filename, false);
    }
}
