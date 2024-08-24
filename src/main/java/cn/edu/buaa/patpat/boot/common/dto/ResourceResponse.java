package cn.edu.buaa.patpat.boot.common.dto;

import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

public class ResourceResponse {
    private ResourceResponse() {}

    public static ResponseEntity<Resource> ok(Resource resource) {
        String filename = resource.getFilename();
        if (filename == null) {
            throw new InternalServerErrorException("Resource filename is null");
        }
        return ok(resource, filename);
    }

    public static ResponseEntity<Resource> ok(Resource resource, String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", UriUtils.encodePath(filename, StandardCharsets.UTF_8)))
                .body(resource);
    }
}
