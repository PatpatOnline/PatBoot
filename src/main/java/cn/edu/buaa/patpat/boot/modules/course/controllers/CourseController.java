package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/course")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course", description = "Course management API")
public class CourseController extends BaseController {
    private final CourseService courseService;
    private final ICookieSetter courseCookieSetter;

    @PostMapping("create")
    @Operation(summary = "Create a new course", description = "Teacher creates a new course")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TEACHER)
    public DataResponse<Course> create(
            @RequestBody @Valid CreateCourseRequest request,
            BindingResult bindingResult,
            HttpServletRequest servletRequest
    ) {
        Course course = courseService.create(request);
        log.info("Created course: {}", course);
        return DataResponse.ok(course);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete a course", description = "Teacher deletes a course")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TEACHER)
    @ValidateCourse
    public DataResponse<Course> delete(
            @PathVariable int id,
            @CourseId Integer courseId,
            HttpServletRequest servletRequest
    ) {
        if (id == 1) {
            throw new ForbiddenException("Cannot delete the default course");
        } else if (id == courseId) {
            throw new ForbiddenException("Cannot delete the current course");
        }

        Course course = courseService.delete(id);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        log.info("Deleted course: {}", course);
        return DataResponse.ok(course);
    }

    @PutMapping("update")
    @Operation(summary = "Update a course", description = "Teacher updates a course, use null to keep the original value")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TEACHER)
    @ValidateCourse
    public DataResponse<Course> update(
            @RequestBody @Valid UpdateCourseRequest request,
            @CourseId Integer courseId,
            BindingResult bindingResult,
            HttpServletRequest servletRequest
    ) {
        if (courseId == 1) {
            throw new ForbiddenException("Cannot update the default course");
        }
        Course course = courseService.update(courseId, request);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        log.info("Updated course: {}", course);
        return DataResponse.ok(course);
    }

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
