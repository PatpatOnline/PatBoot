/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentImportView implements Serializable {
    /**
     * Student ID.
     */
    private int id;

    private int accountId;
    private int courseId;
    private int teacherId;

    private String buaaId;
}
