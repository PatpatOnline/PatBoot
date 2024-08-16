package cn.edu.buaa.patpat.boot.modules.judge.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.judge.services.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/submission")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Submission", description = "Submission API")
public class SubmissionController extends BaseController {

    private final SubmissionService submissionService;

    /**
     * Although this is a GET method, it will modify the state of the server.
     * If the latest submission is timed out (no response from the judge server for long),
     * the submission will be deleted, as if it never existed.
     */
    @GetMapping("query/{id}")
    @Operation(summary = "Get the latest submission", description = "Get the latest submission of a problem")
    public DataResponse<SubmissionDto> query(
            @PathVariable int id,
            AuthPayload auth
    ) {
        var dto = submissionService.getLastSubmission(id, auth.getId());
        return DataResponse.ok(dto);
    }
}
