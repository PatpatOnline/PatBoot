package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JudgeRequestDto {
    private int id;
    private int problemId;
    private String language;

    private LocalDateTime submitTime;

    private JudgePayload payload;
}
