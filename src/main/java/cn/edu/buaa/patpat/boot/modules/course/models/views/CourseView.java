/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseView implements Serializable {
    private int id;
    private String name;
    private String code;
    private String semester;
    private boolean active;

    private String tutorial;
}
