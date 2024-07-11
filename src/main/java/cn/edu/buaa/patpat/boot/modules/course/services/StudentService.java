package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.modules.course.services.impl.StudentImporter;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentImporter studentImporter;
    private final StreamApi streamApi;

    @Async
    public void importStudents(String buaaId, int courseId, String excelPath, boolean clean) {
        var response = studentImporter.importStudents(courseId, excelPath, clean);
        log.info("Student imported for course {}: {}", courseId, response);
        streamApi.send(buaaId, response.toWebSocketPayload());
        try {
            Medias.remove(excelPath);
        } catch (IOException e) {
            log.error("IO exception when importing students: {}", e.getMessage());
        }
    }
}
