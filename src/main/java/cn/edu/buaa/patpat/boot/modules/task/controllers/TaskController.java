/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.problem.dto.ProblemDto;
import cn.edu.buaa.patpat.boot.modules.task.dto.DownloadTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.SubmitTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.TaskScoreDto;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskProblemView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskView;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskService;
import cn.edu.buaa.patpat.boot.modules.task.services.impl.IterationService;
import cn.edu.buaa.patpat.boot.modules.task.services.impl.LabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task", description = "Task API")
public class TaskController extends BaseController {
    private final TaskService taskService;
    private final LabService labService;
    private final IterationService iterationService;

    @GetMapping("{type}/query")
    @Operation(summary = "Get all visible tasks", description = "Get all visible tasks (lab or iter)")
    @ValidateCourse
    public DataResponse<List<TaskListView>> query(
            @PathVariable String type,
            @CourseId Integer courseId
    ) {
        var tasks = taskService.query(courseId, TaskTypes.fromString(type), true);
        return DataResponse.ok(tasks);
    }

    @GetMapping("{type}/query/{id}")
    @Operation(summary = "Get task by id", description = "Get task by id, T.A. get any, student return forbidden if not started or ended")
    @ValidateCourse
    @ValidatePermission
    public DataResponse<TaskView> query(
            @PathVariable String type,
            @PathVariable Integer id,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        // validate time and visible if is student
        var task = taskService.query(
                id,
                courseId,
                TaskTypes.fromString(type),
                auth.isTa());
        return DataResponse.ok(task);
    }

    @PostMapping("lab/submit/{id}")
    @Operation(summary = "Submit Lab report", description = "Student submit Lab report")
    @ValidateMultipartFile(extensions = { "zip", "pdf", "md", "doc", "docx", "txt" }, maxSize = 32)
    @ValidatePermission
    @ValidateCourse
    public DataResponse<TaskScoreDto> submit(
            @PathVariable int id,
            @RequestParam MultipartFile file,
            AuthPayload auth,
            CoursePayload course
    ) {
        var request = new SubmitTaskRequest(id, auth, course, file);
        TaskScore score = labService.submit(request);
        TaskScoreDto dto = mappers.map(score, TaskScoreDto.class);
        dto.initFilename();
        return DataResponse.ok(M("judge.submit.success"), dto);
    }

    @PostMapping("iter/submit/{id}")
    @Operation(summary = "Submit Iteration code", description = "Student submit Iteration code")
    @ValidateMultipartFile(extensions = { "zip", "java" }, maxSize = 1)
    @ValidatePermission
    @ValidateCourse
    public DataResponse<SubmissionDto> submit(
            @PathVariable int id,
            @RequestParam String language,
            @RequestParam MultipartFile file,
            AuthPayload auth,
            CoursePayload course
    ) {
        if (Strings.isNullOrBlank(language)) {
            language = "17";
        } else if (language.length() > 3) {
            throw new BadRequestException(M("judge.language.invalid"));
        }

        var request = new SubmitTaskRequest(id, auth, course, file);
        SubmissionDto dto = iterationService.submit(request, language);

        return DataResponse.ok(dto);
    }

    @GetMapping("{type}/score/{id}")
    @Operation(summary = "Get task score", description = "Student get task score")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<TaskScoreDto> score(
            @PathVariable String type,
            @PathVariable int id,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var score = taskService.findScore(
                id,
                courseId,
                TaskTypes.fromString(type),
                auth.getId(),
                auth.isStudent()
        );

        if (score == null) {
            return DataResponse.ok(null);
        }
        var dto = mappers.map(score, TaskScoreDto.class);
        dto.initFilename();

        return DataResponse.ok(dto);
    }

    @GetMapping("{type}/download/{id}")
    @Operation(summary = "Download Lab report", description = "Student downloads their own Lab report")
    @ValidatePermission
    @ValidateCourse
    public ResponseEntity<Resource> download(
            @PathVariable String type,
            @PathVariable int id,
            AuthPayload auth,
            CoursePayload course
    ) {
        TaskTypes.fromString(type);

        var request = new DownloadTaskRequest(id, auth, course);
        Resource resource = labService.download(request);

        return ResourceResponse.ok(resource);
    }

    @GetMapping("{type}/problems/{id}")
    @Operation(summary = "Get problems", description = "Get problems of a task with score")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<List<TaskProblemView>> getProblems(
            @PathVariable String type,
            @PathVariable int id,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var problems = taskService.getProblems(
                id,
                courseId,
                TaskTypes.fromString(type),
                auth.getId(),
                auth.isStudent());
        return DataResponse.ok(problems);
    }

    @GetMapping("iter/problem/{id}")
    @Operation(summary = "Get Iteration problem", description = "Get Iteration problem content")
    @ValidatePermission
    @ValidateCourse
    public DataResponse<ProblemDto> getIterProblem(
            @PathVariable int id,
            AuthPayload auth,
            @CourseId Integer courseId
    ) {
        var problem = iterationService.getIterationProblem(id, courseId, auth);
        return DataResponse.ok(problem);
    }
}
