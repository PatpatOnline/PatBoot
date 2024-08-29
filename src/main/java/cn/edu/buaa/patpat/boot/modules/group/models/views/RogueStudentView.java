/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class RogueStudentView implements Serializable {
    private int accountId;
    private String buaaId;
    private String name;

    private int teacherId;
}
