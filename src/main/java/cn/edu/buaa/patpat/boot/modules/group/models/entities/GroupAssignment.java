/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class GroupAssignment implements Serializable {
    private int courseId;
    private String comment;

    private boolean visible;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
