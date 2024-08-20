package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupAssignment {
    private int courseId;
    private String comment;

    private boolean visible;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
