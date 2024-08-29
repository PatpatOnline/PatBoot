/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentExportView implements Serializable {
    private String buaaId;
    private String name;
    private int gender;
    private String school;
    private String major;
    private String className;
    private boolean repeat;
}
