package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.annotations.RequestValidation;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.modules.account.dto.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.annotations.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.annotations.CourseValidation;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AuthApi authApi;
    private final ICookieSetter courseCookieSetter;

    @PostMapping("create")
    @Operation(summary = "Create a new course", description = "Teacher creates a new course")
    @RequestValidation(authLevel = AuthLevel.TEACHER)
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
    @RequestValidation(authLevel = AuthLevel.TEACHER)
    @CourseValidation
    public DataResponse<Course> delete(
            @PathVariable int id,
            @CourseId int courseId,
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

    @PutMapping("update/{id}")
    @Operation(summary = "Update a course", description = "Teacher updates a course, use null to keep the original value")
    @RequestValidation(authLevel = AuthLevel.TEACHER)
    public DataResponse<Course> update(
            @PathVariable int id,
            @RequestBody @Valid UpdateCourseRequest request,
            BindingResult bindingResult,
            HttpServletRequest servletRequest
    ) {
        if (id == 1) {
            throw new ForbiddenException("Cannot update the default course");
        }

        Course course = courseService.update(id, request);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        log.info("Updated course: {}", course);
        return DataResponse.ok(course);
    }

    @GetMapping("all")
    @Operation(summary = "Get all courses", description = "T.A. gets all courses")
    @RequestValidation(authLevel = AuthLevel.LOGIN)
    public DataResponse<List<Course>> getAll(
            AuthPayload auth,
            HttpServletRequest servletRequest
    ) {
        return DataResponse.ok(courseService.getAll(auth));
    }

    @PostMapping("select/{id}")
    @Operation(summary = "Select a course", description = "User selects the current course")
    @RequestValidation(authLevel = AuthLevel.LOGIN)
    public DataResponse<Course> select(
            @PathVariable int id,
            AuthPayload auth
    ) {
        Course course = courseService.tryGetCourse(auth, id);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        courseCookieSetter.set(String.valueOf(course.getId()));

        return DataResponse.ok(course);
    }
}
