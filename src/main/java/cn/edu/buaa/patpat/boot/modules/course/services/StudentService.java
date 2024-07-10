package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.services.impl.StudentImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentImporter studentImporter;

    public ImportStudentResponse importStudents(int courseId, String excelPath, boolean clean) {
        return studentImporter.importStudents(courseId, excelPath, clean);
    }
}
