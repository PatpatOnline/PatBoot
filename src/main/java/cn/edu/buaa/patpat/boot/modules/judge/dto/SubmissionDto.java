package cn.edu.buaa.patpat.boot.modules.judge.dto;

import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionDto extends JudgeTimestamp {
    private int id;

    private String language;
    private int score;
    private TestResult result;

    public static SubmissionDto of(Submission submission, Mappers mappers) {
        SubmissionDto dto = mappers.map(submission, SubmissionDto.class);
        if (submission.getData() != null) {
            dto.setResult(mappers.fromJson(submission.getData(), TestResult.class, null));
        }
        return dto;
    }
}
