/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.dto;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionAdminDto extends SubmissionDto {
    private int accountId;
    private String buaaId;
    private int problemId;

    public static SubmissionAdminDto of(Submission submission, Mappers mappers) {
        SubmissionAdminDto dto = mappers.map(submission, SubmissionAdminDto.class);
        if (submission.getData() != null) {
            dto.setResult(mappers.fromJson(submission.getData(), TestResult.class, null));
        }
        return dto;
    }
}
