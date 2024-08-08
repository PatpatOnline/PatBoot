package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.aspect.Page;
import cn.edu.buaa.patpat.boot.aspect.PageSize;
import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.aspect.ValidatePagination;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateStudentRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentFilter;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentDetailView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentListView;
import cn.edu.buaa.patpat.boot.modules.course.services.ImportService;
import cn.edu.buaa.patpat.boot.modules.course.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/student")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Admin", description = "Admin student management API")
public class StudentAdminController extends BaseController {
    private final BucketApi bucketApi;
    private final ImportService importService;
    private final StudentService studentService;

    @PostMapping("/import/async")
    @Operation(summary = "Import students asynchronously", description = "T.A. imports students from an Excel file, results will be returned via WebSocket.")
    @ValidateMultipartFile(maxSize = 4, extensions = { "xlsx", "xls" })
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse(allowRoot = false)
    public MessageResponse importStudentsAsync(
            @RequestParam("file") MultipartFile file,
            @RequestParam boolean clean,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        String record = bucketApi.toRandomRecord(file.getOriginalFilename());
        String path = bucketApi.recordToPrivatePath(record);
        try {
            Medias.save(path, file);
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to save the file");
        }

        // Import students asynchronously.
        importService.importStudentsAsync(auth.getBuaaId(), courseId, path, clean);

        return MessageResponse.ok(M("student.import.progress"));
    }

    @PostMapping("/import")
    @Operation(summary = "Import students synchronously", description = "T.A. imports students from an Excel file results will be returned synchronously.")
    @ValidateMultipartFile(maxSize = 4, extensions = { "xlsx", "xls" })
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse(allowRoot = false)
    public DataResponse<ImportStudentResponse> importStudents(
            @RequestParam("file") MultipartFile file,
            @RequestParam boolean clean,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        String record = bucketApi.toRandomRecord(file.getOriginalFilename());
        String path = bucketApi.recordToPrivatePath(record);
        try {
            Medias.save(path, file);
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to save the file");
        }

        // Import students synchronously.
        var response = importService.importStudents(courseId, path, clean);

        return DataResponse.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get student detail", description = "Get student detail by student ID")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<StudentDetailView> detail(
            @PathVariable int id
    ) {
        var view = studentService.getDetail(id);
        return DataResponse.ok(view);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update student", description = "Update student information by student ID")
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<StudentListView> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateStudentRequest request
    ) {
        var student = studentService.update(id, request);
        var view = studentService.query(student.getId());
        return DataResponse.ok(M("student.update.success"), view);
    }

    @GetMapping("query")
    @Operation(summary = "Query students", description = "Query students in current course with filters")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    @ValidatePagination
    public DataResponse<PageListDto<StudentListView>> query(
            @CourseId Integer courseId,
            @RequestParam(name = "p") @Page int page,
            @RequestParam(name = "ps") @PageSize int pageSize,
            @RequestParam(required = false) String buaaId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer teacherId
    ) {
        StudentFilter filter = new StudentFilter(buaaId, name, teacherId);
        var response = studentService.query(courseId, page, pageSize, filter);
        return DataResponse.ok(response);
    }
}
