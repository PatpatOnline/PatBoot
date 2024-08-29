/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.modules.course.api.CourseApi;
import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.services.impl.StudentImporter;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {
    private final StudentImporter studentImporter;
    private final StreamApi streamApi;
    private final CourseApi courseApi;

    @Async
    public void importStudentsAsync(String buaaId, int courseId, String excelPath, boolean clean) {
        var response = importStudents(courseId, excelPath, clean);
        streamApi.send(buaaId, response.toWebSocketPayload());
    }

    public ImportStudentResponse importStudents(int courseId, String excelPath, boolean clean) {
        LocalDateTime timestamp = LocalDateTime.now();
        log.info("Importing students for course {} started at {}", courseId, timestamp);

        ImportStudentResponse response = studentImporter.importStudents(courseId, excelPath, clean);
        log.info("Student imported for course {}: {}", courseId, response);
        try {
            Medias.remove(excelPath);
        } catch (IOException e) {
            log.error("IO exception when importing students", e);
        }

        log.info("Importing students for course {} at {} finished", courseId, timestamp);
        return response;
    }

    public Resource exportStudents(int courseId, int teacherId) {
        String courseName = courseApi.getCourseName(courseId);
        return studentImporter.exportStudents(courseId, courseName, teacherId);
    }
}
