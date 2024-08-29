/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseDescriptor implements Serializable {
    private int id;
    private String description;
    private int score;
}
