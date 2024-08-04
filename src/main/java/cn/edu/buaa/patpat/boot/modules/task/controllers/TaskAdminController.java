package cn.edu.buaa.patpat.boot.modules.task.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateParameters;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.task.dto.CreateTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.TaskDto;
import cn.edu.buaa.patpat.boot.modules.task.dto.UpdateTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/task")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Admin", description = "Task Admin API")
public class TaskAdminController extends BaseController {
    private final TaskService taskService;

    @PostMapping("{type}/create")
    @Operation(summary = "Create Task", description = "Create a new task (lab or iter)")
    @ValidateParameters
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<TaskDto> create(
            @PathVariable String type,
            @RequestBody @Valid CreateTaskRequest request,
            @CourseId Integer courseId
    ) {
        return create(TaskTypes.fromString(type), courseId, request);
    }

    private DataResponse<TaskDto> create(int type, int courseId, CreateTaskRequest request) {
        Task task = taskService.create(type, courseId, request);
        return DataResponse.ok(
                M("task.create.success"),
                mappers.map(task, TaskDto.class));
    }

    @PutMapping("{type}/update/{id}")
    @Operation(summary = "Update Task", description = "Update an existing task (lab or iter)")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public DataResponse<TaskDto> update(
            @PathVariable String type,
            @PathVariable int id,
            @RequestBody @Valid UpdateTaskRequest request,
            @CourseId Integer courseId
    ) {
        return update(id, courseId, TaskTypes.fromString(type), request);
    }

    private DataResponse<TaskDto> update(int id, int courseId, int type, UpdateTaskRequest request) {
        Task task = taskService.update(id, courseId, type, request);
        return DataResponse.ok(
                M("task.update.success"),
                mappers.map(task, TaskDto.class));
    }

    @DeleteMapping("{type}/delete/{id}")
    @Operation(summary = "Delete Task", description = "Delete an existing task (lab or iter)")
    @ValidatePermission(AuthLevel.TA)
    @ValidateCourse
    public MessageResponse delete(
            @PathVariable String type,
            @PathVariable int id,
            @CourseId Integer courseId
    ) {
        return delete(id, courseId, TaskTypes.fromString(type));
    }

    private MessageResponse delete(int id, int courseId, int type) {
        taskService.delete(id, courseId, type);
        return MessageResponse.ok(M("task.delete.success"));
    }
}
