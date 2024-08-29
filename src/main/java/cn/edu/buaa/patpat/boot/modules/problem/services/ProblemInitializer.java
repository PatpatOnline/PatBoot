/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.services;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.problem.exceptions.ProblemInitializeException;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.ProblemDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
public class ProblemInitializer {
    private static final Logger log = LoggerFactory.getLogger(ProblemInitializer.class);
    private final Mappers mappers;
    private final BucketApi bucketApi;

    public ProblemInitializer(Mappers mappers, BucketApi bucketApi) {
        this.mappers = mappers;
        this.bucketApi = bucketApi;
    }

    public InitializeResult initialize(MultipartFile file) throws ProblemInitializeException {
        String archivePath = extractZipFile(file);
        String problemPath = findProblem(archivePath);
        try {
            ProblemDescriptor descriptor = validateProblem(problemPath);
            return new InitializeResult(descriptor, archivePath, problemPath);
        } catch (ProblemInitializeException e) {
            Medias.removeSilently(archivePath);
            throw e;
        }
    }

    /**
     * Finalize the problem by moving the files to the correct location.
     * If the problemId is greater than 0, the problem will be moved to the correct location.
     * Otherwise, the problem will be removed as error occurs.
     *
     * @param result    The result of the initialization.
     * @param problemId The ID of the problem.
     * @throws ProblemInitializeException If an error occurs.
     */
    public void finalize(InitializeResult result, int problemId) throws ProblemInitializeException {
        String path = getProblemPath(problemId);
        try {
            if (problemId > 0) {
                Medias.ensureParentPath(path);
                Medias.remove(path);
                Files.move(Path.of(result.problemPath), Path.of(path), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new ProblemInitializeException(M("system.error.io"));
        } finally {
            Medias.removeSilently(result.archivePath);
        }
    }

    public Resource download(int problemId) {
        String path = getProblemPath(problemId);
        if (!Medias.exists(Path.of(path))) {
            throw new NotFoundException(M("problem.exists.not"));
        }
        String zipFile = bucketApi.getRandomTempFile("zip");
        try {
            Zips.zip(path, zipFile, false);
            return Medias.loadAsResource(zipFile, true);
        } catch (IOException e) {
            throw new InternalServerErrorException(M("problem.download.error"));
        }
    }

    private String getProblemPath(int problemId) {
        String record = bucketApi.toRecord(Globals.PROBLEM_TAG, String.valueOf(problemId));
        return bucketApi.recordToPrivatePath(record);
    }

    private String extractZipFile(MultipartFile file) throws ProblemInitializeException {
        // save zip file
        String record = bucketApi.toRandomRecord(Globals.TEMP_TAG, file.getOriginalFilename());
        String zipPath = bucketApi.recordToPrivatePath(record);
        try {
            Medias.save(zipPath, file);
        } catch (IOException e) {
            throw new ProblemInitializeException(M("system.error.io"));
        }

        String path = bucketApi.getRandomTempPath();
        try {
            Zips.unzip(zipPath, path);
        } catch (IOException e) {
            throw new ProblemInitializeException(M("system.error.io"));
        } finally {
            Medias.removeSilently(zipPath);
        }

        return path;
    }

    private String findProblem(String path) throws ProblemInitializeException {
        Path problemYamlPath = Path.of(path, "problem.yaml");
        if (Medias.exists(problemYamlPath)) {
            return path;
        }
        // check if the path contains only one directory
        try (var stream = Files.list(Path.of(path))) {
            var files = stream.toArray(Path[]::new);
            if (files.length != 1) {
                throw new ProblemInitializeException("Invalid problem directory");
            }
            return findProblem(files[0].toString());
        } catch (IOException e) {
            throw new ProblemInitializeException("Failed to list directory");
        }
    }

    private ProblemDescriptor validateProblem(String path) throws ProblemInitializeException {
        // Read problem.yaml
        Path problemYamlPath = Path.of(path, "problem.yaml");
        if (!Medias.exists(problemYamlPath)) {
            throw new ProblemInitializeException(M("problem.init.error.format.exists.not"));
        }

        // Read problem.yaml
        ProblemDescriptor descriptor;
        try {
            descriptor = ProblemDescriptor.loadFromYamlFile(problemYamlPath.toString(), mappers);
        } catch (IOException e) {
            log.error("Failed to load problem.yaml: {}", e.getMessage());
            throw new ProblemInitializeException(M("problem.init.error.format.invalid"));
        }
        descriptor.validate();

        // Check init.
        if (descriptor.isInit()) {
            if (!Medias.exists(Path.of(path, "init"))) {
                throw new ProblemInitializeException(M("problem.init.error.format.init"));
            }
        }

        // Check test cases.
        for (var testCase : descriptor.getCases()) {
            String baseName = Path.of(path, "tests", String.valueOf(testCase.getId())).toString();
            if (!Medias.exists(baseName + ".in")) {
                throw new ProblemInitializeException(MessageFormat.format(M("problem.init.error.missing"), ".in", testCase.getId()));
            }
            if (!Medias.exists(baseName + ".out")) {
                throw new ProblemInitializeException(MessageFormat.format(M("problem.init.error.missing"), ".out", testCase.getId()));
            }
        }

        return descriptor;
    }

    public record InitializeResult(ProblemDescriptor descriptor, String archivePath, String problemPath) {}
}
