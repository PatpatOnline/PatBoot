package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.Tuple;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.CourseMapper;
import cn.edu.buaa.patpat.boot.modules.course.services.impl.StudentImporter;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {
    private final StudentImporter studentImporter;
    private final StreamApi streamApi;
    private final CourseMapper courseMapper;

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
            log.error("IO exception when importing students: {}", e.getMessage());
        }

        log.info("Importing students for course {} at {} finished", courseId, timestamp);
        return response;
    }

    public Tuple<Resource, String> exportStudents(int courseId, int teacherId) {
        Course course = courseMapper.findName(courseId);
        if (course == null) {
            throw new NotFoundException(M("course.not.found"));
        }
        LocalDateTime timestamp = LocalDateTime.now();
        Resource resource = studentImporter.exportStudents(courseId, course.getName(), teacherId, timestamp);
        String filename = String.format("%s-学生名单-%s.xlsx",
                course.getName(),
                timestamp.format(DateTimeFormatter.ofPattern(Globals.FILE_DATE_FORMAT)));

        return Tuple.of(resource, filename);
    }
}
