/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmitRequest {
    private int accountId;
    private String buaaId;
    private int courseId;
    private int problemId;
    private String language;

    private LocalDateTime submitTime;

    private String filePath;

    /**
     * If taskId is not 0, then the submission should update the task score.
     * This is for Iteration tasks and should be set manually.
     */
    private int taskId;

    public SubmitRequest(int accountId, String buaaId, int courseId, int problemId, String language, String filePath) {
        this.accountId = accountId;
        this.buaaId = buaaId;
        this.courseId = courseId;
        this.problemId = problemId;
        this.language = language;
        this.filePath = filePath;
        this.submitTime = LocalDateTime.now();
    }
}
