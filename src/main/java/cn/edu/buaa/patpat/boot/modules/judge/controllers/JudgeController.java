package cn.edu.buaa.patpat.boot.modules.judge.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmitRequest;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import cn.edu.buaa.patpat.boot.modules.judge.services.JudgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/judge")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Judge", description = "Judge API")
public class JudgeController extends BaseController {
    private final JudgeService judgeService;

    @PostMapping("/submit/{problemId}")
    @Operation(summary = "Submit a solution to a problem", description = "Student submits a solution to a problem to invoke a judge")
    @ValidateMultipartFile(maxSize = 1, extensions = { "java", "zip" })
    @ValidateCourse
    @ValidatePermission
    public DataResponse<SubmissionDto> submit(
            @PathVariable int problemId,
            @RequestParam String language,
            @RequestParam MultipartFile file,
            @CourseId Integer courseId,
            AuthPayload auth
    ) {
        if (Strings.isNullOrBlank(language)) {
            language = "17";
        } else if (language.length() > 3) {
            throw new BadRequestException(M("judge.language.invalid"));
        }

        judgeService.checkProblem(problemId, false);
        judgeService.checkLastSubmission(problemId, auth.getId());

        String filePath = judgeService.saveSubmissionInTemp(file);
        var request = new SubmitRequest(auth.getId(), auth.getBuaaId(), problemId, courseId, language, filePath);
        Submission submission = judgeService.submit(request, true);

        return DataResponse.ok(
                M("judge.submit.success"),
                SubmissionDto.of(submission, mappers));
    }
}
