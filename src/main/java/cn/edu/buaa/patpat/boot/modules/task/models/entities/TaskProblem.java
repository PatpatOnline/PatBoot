/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskProblem implements Serializable {
    private int taskId;
    private int problemId;
    private int order;
}
