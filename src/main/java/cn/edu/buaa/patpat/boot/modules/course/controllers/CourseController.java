package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/course")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course", description = "Student course API")
public class CourseController extends BaseController {
    private final CourseService courseService;
    private final ICookieSetter courseCookieSetter;

    @GetMapping("all")
    @Operation(summary = "Get all courses", description = "T.A. gets all courses")
    @ValidatePermission(AuthLevel.LOGIN)
    public DataResponse<List<Course>> getAll(
            AuthPayload auth
    ) {
        return DataResponse.ok(courseService.getAll(auth));
    }

    @PostMapping("select/{id}")
    @Operation(summary = "Select a course", description = "User selects the current course")
    @ValidatePermission
    public DataResponse<Course> select(
            @PathVariable int id,
            AuthPayload auth,
            HttpServletResponse servletResponse
    ) {
        Course course = courseService.tryGetCourse(auth, id);
        if (course == null) {
            throw new NotFoundException(M("course.exists.not"));
        }

        Cookie cookie = courseCookieSetter.set(String.valueOf(course.getId()));
        servletResponse.addCookie(cookie);

        return DataResponse.ok(
                M("course.select.success"),
                course);
    }

    @GetMapping("")
    @Operation(summary = "Get the current course", description = "Get the current course")
    @ValidateCourse
    public DataResponse<Course> current(
            @CourseId Integer courseId,
            HttpServletResponse servletResponse
    ) {
        Course course = courseService.findById(courseId);
        if (course == null) {
            Cookie cookie = courseCookieSetter.clean();
            servletResponse.addCookie(cookie);
            throw new NotFoundException(M("course.exists.not"));
        }
        return DataResponse.ok(course);
    }
}
