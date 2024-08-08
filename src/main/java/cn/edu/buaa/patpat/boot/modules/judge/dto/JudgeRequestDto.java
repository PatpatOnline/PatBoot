package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JudgeRequestDto {
    private int id;
    private int problemId;
    private String language;

    private LocalDateTime submitTime;

    /**
     * The path to this submission.
     * It is the bridge between PatBoot and PatJudge.
     * This path will also be mapped to PatJudge to access the file.
     */
    private String record;

    private JudgePayload payload;
}
