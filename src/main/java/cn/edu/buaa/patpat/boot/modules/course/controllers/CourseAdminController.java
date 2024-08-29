/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CourseTutorialDto;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseTutorialRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.CourseTutorial;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/course")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Course Admin", description = "Admin course management API")
public class CourseAdminController extends BaseController {
    private final CourseService courseService;

    @PostMapping("create")
    @Operation(summary = "Create a new course", description = "Teacher creates a new course")
    @ValidatePermission(AuthLevel.TEACHER)
    public DataResponse<Course> create(
            @RequestBody @Valid CreateCourseRequest request
    ) {
        Course course = courseService.create(request);
        log.info("Created course: {}", course);
        return DataResponse.ok(
                M("course.create.success"),
                course);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete a course", description = "Teacher deletes a course")
    @ValidatePermission(AuthLevel.TEACHER)
    @ValidateCourse
    public DataResponse<Course> delete(
            @PathVariable int id,
            @CourseId Integer courseId
    ) {
        if (id == 1) {
            throw new ForbiddenException(M("course.delete.default"));
        } else if (id == courseId) {
            throw new ForbiddenException(M("course.delete.current"));
        }

        Course course = courseService.delete(id);
        if (course == null) {
            throw new NotFoundException(M("course.exists.not"));
        }
        log.info("Deleted course: {}", course);
        return DataResponse.ok(
                M("course.delete.success"),
                course);
    }

    @PutMapping("update")
    @Operation(summary = "Update a course", description = "Teacher updates a course, use null to keep the original value")
    @ValidatePermission(AuthLevel.TEACHER)
    @ValidateCourse(allowRoot = false)
    public DataResponse<Course> update(
            @RequestBody @Valid UpdateCourseRequest request,
            @CourseId Integer courseId
    ) {
        Course course = courseService.update(courseId, request);
        if (course == null) {
            throw new NotFoundException(M("course.exists.not"));
        }
        log.info("Updated course: {}", course);
        return DataResponse.ok(
                M("course.update.success"),
                course);
    }

    @PostMapping("tutorial/update")
    @Operation(summary = "Update course tutorial", description = "T.A. updates the course tutorial")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<CourseTutorialDto> updateTutorial(
            @RequestBody @Valid UpdateCourseTutorialRequest request,
            @CourseId Integer courseId
    ) {
        CourseTutorial tutorial = courseService.updateTutorial(courseId, request.getUrl());
        return DataResponse.ok(
                M("course.tutorial.update.success"),
                mappers.map(tutorial, CourseTutorialDto.class));
    }

    @DeleteMapping("tutorial/delete")
    @Operation(summary = "Delete course tutorial", description = "T.A. deletes the course tutorial")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public MessageResponse deleteTutorial(
            @CourseId Integer courseId
    ) {
        courseService.deleteTutorial(courseId);
        return MessageResponse.ok(M("course.tutorial.delete.success"));
    }

    @GetMapping("tutorial")
    @Operation(summary = "Get course tutorial", description = "T.A. gets the current course tutorial")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<CourseTutorialDto> getTutorial(
            @CourseId Integer courseId
    ) {
        CourseTutorial tutorial = courseService.findTutorial(courseId);
        return DataResponse.ok(tutorial == null ? null : mappers.map(tutorial, CourseTutorialDto.class));
    }
}
