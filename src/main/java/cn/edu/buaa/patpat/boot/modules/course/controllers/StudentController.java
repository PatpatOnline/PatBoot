package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentView;
import cn.edu.buaa.patpat.boot.modules.course.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student", description = "Student management API")
public class StudentController extends BaseController {
    private final StudentService studentService;

    @GetMapping("self")
    @Operation(summary = "Get student detail", description = "Get student detail of the current user")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<StudentView> detail(
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var view = studentService.getDetail(auth.getId(), courseId);
        return DataResponse.ok(view);
    }
}
