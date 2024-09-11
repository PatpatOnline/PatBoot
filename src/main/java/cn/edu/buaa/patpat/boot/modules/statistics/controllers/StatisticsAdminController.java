/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.statistics.dto.ScoreConfigDto;
import cn.edu.buaa.patpat.boot.modules.statistics.dto.UpdateScoreConfigRequest;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.GroupScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.StudentIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.services.ScoreConfigService;
import cn.edu.buaa.patpat.boot.modules.statistics.services.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/statistics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Statistics Admin", description = "Statistics Admin API for score")
public class StatisticsAdminController extends BaseController {
    private final StatisticsService statisticsService;
    private final ScoreConfigService scoreConfigService;

    @GetMapping("config")
    @Operation(summary = "Get score config", description = "Get score config of course")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<ScoreConfigDto> getScoreConfig(
            @CourseId Integer courseId
    ) {
        var config = scoreConfigService.get(courseId);
        return DataResponse.ok(mappers.map(config, ScoreConfigDto.class));
    }

    @PostMapping("config/update")
    @Operation(summary = "Update score config", description = "Update score config of course")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<ScoreConfigDto> updateScoreConfig(
            @RequestBody @Valid UpdateScoreConfigRequest request,
            @CourseId Integer courseId
    ) {
        var config = scoreConfigService.update(courseId, request);
        return DataResponse.ok(mappers.map(config, ScoreConfigDto.class));
    }

    @GetMapping("students")
    @Operation(summary = "Get all students", description = "Get all students in course to show statistics")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<List<StudentIndexView>> queryStudents(
            @CourseId Integer courseId
    ) {
        var students = statisticsService.queryStudents(courseId);
        return DataResponse.ok(students);
    }

    @GetMapping("tasks")
    @Operation(summary = "Get all tasks", description = "Get all tasks in course to show statistics")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<List<TaskIndexView>> queryTasks(
            @CourseId Integer courseId
    ) {
        var tasks = statisticsService.queryTasks(courseId);
        return DataResponse.ok(tasks);
    }

    @GetMapping("tasks/{id}")
    @Operation(summary = "Get task scores", description = "Get all students' scores (submitted) in a task")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<List<TaskScoreIndexView>> queryTaskScores(
            @PathVariable int id
    ) {
        var taskScores = statisticsService.queryTaskScores(id);
        return DataResponse.ok(taskScores);
    }

    /**
     * If the group assignment of the current course is not created
     * or is not visible, will return null.
     */
    @GetMapping("groups")
    @Operation(summary = "Get group scores", description = "Get all groups' scores in course")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<List<GroupScoreIndexView>> queryGroupScores(
            @CourseId Integer courseId
    ) {
        var groupScores = statisticsService.queryGroupScores(courseId);
        return DataResponse.ok(groupScores);
    }

    @GetMapping("export")
    @Operation(summary = "Export statistics", description = "Export score statistics of course")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public ResponseEntity<Resource> export(
            @CourseId Integer courseId
    ) {
        Resource resource = statisticsService.export(courseId);
        return ResourceResponse.ok(resource);
    }
}
