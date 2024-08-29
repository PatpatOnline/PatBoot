/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskIndexView implements Serializable {
    private int id;
    private int type;
    private String title;
}
