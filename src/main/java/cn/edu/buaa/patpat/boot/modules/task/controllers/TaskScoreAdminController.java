package cn.edu.buaa.patpat.boot.modules.task.controllers;

import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.task.dto.GradeRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskAdminService;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/task/score")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Score Admin", description = "Score related API")
public class TaskScoreAdminController extends BaseController {
    private final TaskAdminService taskAdminService;
    private final TaskService taskService;

    @GetMapping("{type}/download/{id}/{studentId}")
    @Operation(summary = "Download task submission", description = "Download a task submission of a student")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public ResponseEntity<Resource> download(
            @PathVariable String type,
            @PathVariable int id,
            @PathVariable int studentId
    ) {
        TaskTypes.fromString(type);
        Resource resource = taskAdminService.download(id, studentId);
        return ResourceResponse.ok(resource);
    }

    @GetMapping("{type}/download/{id}")
    @Operation(summary = "Download all task submissions", description = "Download all task submissions of a task")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public ResponseEntity<Resource> downloadAll(
            @PathVariable String type,
            @PathVariable int id,
            @RequestParam int teacherId,
            @CourseId Integer courseId
    ) {
        Resource resource = taskAdminService.downloadAll(
                id,
                TaskTypes.fromString(type),
                courseId,
                teacherId);
        return ResourceResponse.ok(resource);
    }

    @PostMapping("lab/grade/{labId}")
    @Operation(summary = "Grade Task", description = "Grade a task for a list of students")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public MessageResponse grade(
            @PathVariable int labId,
            @RequestBody @Valid GradeRequest request,
            @CourseId Integer courseId
    ) {
        if (!taskService.exists(labId, courseId, TaskTypes.LAB)) {
            return MessageResponse.notFound(M("task.exists.not", TaskTypes.toString(TaskTypes.LAB)));
        }
        int updated = taskAdminService.batchGrade(labId, request.getScore(), request.getIds());
        return MessageResponse.ok(M("task.lab.grade.success", updated));
    }
}
