/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherIndexView implements Serializable {
    private int id;
    private String name;
}
