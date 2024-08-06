package cn.edu.buaa.patpat.boot.modules.task.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.task.dto.SubmitLabRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.TaskScoreDto;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.services.LabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/task/lab")
@Slf4j
@Tag(name = "Task Lab", description = "Lab API")
public class LabController extends BaseController {
    private final LabService labService;

    @PostMapping("submit/{labId}")
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
        var request = new SubmitLabRequest(labId, auth, course, file);
        TaskScore score = labService.submit(request);
        TaskScoreDto dto = mappers.map(score, TaskScoreDto.class);
        dto.initFilename();
        return DataResponse.ok(dto);
    }
}
