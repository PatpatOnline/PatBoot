/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.api;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmitRequest;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import cn.edu.buaa.patpat.boot.modules.judge.services.JudgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JudgeApi {
    private final JudgeService judgeService;
    private final Mappers mappers;

    public SubmissionDto submit(SubmitRequest request) {
        judgeService.checkProblem(request.getProblemId(), true);
        judgeService.checkLastSubmission(request.getProblemId(), request.getAccountId());
        Submission submission = judgeService.submit(request, false);
        return mappers.map(submission, SubmissionDto.class);
    }
}
