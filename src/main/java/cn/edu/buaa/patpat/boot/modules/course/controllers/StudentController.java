package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student", description = "Student management API")
public class StudentController {
    private final BucketApi bucketApi;
    private final StudentService studentService;

    @PostMapping("/import")
    @Operation(summary = "Import students from an Excel file", description = "T.A. imports students from an Excel file, results will be returned via WebSocket.")
    @ValidateParameters
    @ValidateMultipartFile(maxSize = 4, extensions = { "xlsx", "xls" })
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public MessageResponse importStudents(
            @RequestParam("file") MultipartFile file,
            @RequestParam boolean clean,
            @CourseId Integer courseId,
            AuthPayload auth,
            HttpServletRequest request
    ) {
        String record = bucketApi.toRecord(file.getOriginalFilename());
        String path = bucketApi.recordToPrivatePath(record);
        try {
            Medias.save(path, file);
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to save the file");
        }

        // Import students asynchronously.
        studentService.importStudents(auth.getBuaaId(), courseId, path, clean);

        return MessageResponse.ok("Importing students...");
    }
}
