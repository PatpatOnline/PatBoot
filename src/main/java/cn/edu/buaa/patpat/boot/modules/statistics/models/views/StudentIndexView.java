/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

@Data
public class StudentIndexView {
    private int accountId;
    private String buaaId;
    private String name;
    private int teacherId;
}
