/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

@Data
public class JudgePayload {
    private int accountId;
    private String buaaId;
    private int courseId;

    /**
     * If taskId is not 0, then this submission is related to
     * a task (Iteration) and should update the task's score also.
     */
    private int taskId;
}
