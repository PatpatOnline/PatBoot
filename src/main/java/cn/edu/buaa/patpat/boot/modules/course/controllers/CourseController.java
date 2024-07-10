package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ValidateParameters
    @ValidatePermission(AuthLevel.LOGIN)
    public DataResponse<List<Course>> getAll(
            AuthPayload auth,
            HttpServletRequest servletRequest
    ) {
        return DataResponse.ok(courseService.getAll(auth));
    }

    @PostMapping("select/{id}")
    @Operation(summary = "Select a course", description = "User selects the current course")
    @ValidateParameters
    @ValidatePermission
    public DataResponse<Course> select(
            @PathVariable int id,
            AuthPayload auth,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        Course course = courseService.tryGetCourse(auth, id);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }

        Cookie cookie = courseCookieSetter.set(String.valueOf(course.getId()));
        servletResponse.addCookie(cookie);

        return DataResponse.ok(course);
    }

    @GetMapping("current")
    @Operation(summary = "Get the current course", description = "Get the current course")
    @ValidateCourse
    public DataResponse<Course> current(
            @CourseId Integer courseId,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        Course course = courseService.findById(courseId);
        if (course == null) {
            Cookie cookie = courseCookieSetter.clean();
            servletResponse.addCookie(cookie);
            throw new NotFoundException("Course not found");
        }
        return DataResponse.ok(course);
    }
}
