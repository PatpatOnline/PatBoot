package cn.edu.buaa.patpat.boot.modules.task.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.ResourceResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.task.dto.DownloadTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.SubmitTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.TaskScoreDto;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskView;
import cn.edu.buaa.patpat.boot.modules.task.services.LabService;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task", description = "Task API")
public class TaskController extends BaseController {
    private final TaskService taskService;
    private final LabService labService;

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
    @Operation(summary = "Get task by id", description = "Get task by id, return forbidden if not started or ended")
    @ValidateCourse
    public DataResponse<TaskView> queryById(
            @PathVariable String type,
            @PathVariable Integer id,
            @CourseId Integer courseId
    ) {
        var task = taskService.query(id, courseId, TaskTypes.fromString(type), true);
        return DataResponse.ok(task);
    }

    @PostMapping("lab/submit/{labId}")
    @Operation(summary = "Submit Lab report", description = "Student submit Lab report")
    @ValidateMultipartFile(extensions = { "zip", "pdf", "md", "doc", "docx", "txt" }, maxSize = 10)
    @ValidatePermission
    @ValidateCourse
    public DataResponse<TaskScoreDto> submit(
            @PathVariable int labId,
            @RequestParam MultipartFile file,
            AuthPayload auth,
            CoursePayload course
    ) {
        var request = new SubmitTaskRequest(labId, auth, course, file);
        TaskScore score = labService.submit(request);
        TaskScoreDto dto = mappers.map(score, TaskScoreDto.class);
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
}
