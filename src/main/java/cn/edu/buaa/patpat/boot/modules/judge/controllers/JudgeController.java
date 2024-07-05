package cn.edu.buaa.patpat.boot.modules.judge.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.judge.services.JudgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/judge")
@RequiredArgsConstructor
@Slf4j
public class JudgeController {
    private final JudgeService judgeService;

    @PostMapping("submit/{id}")
    public DataResponse<SubmissionDto> submit(
            @PathVariable("id") int id
    ) {
        SubmissionDto submissionDto = judgeService.initiateSubmission(id);
        return DataResponse.ok(submissionDto);
    }
}
