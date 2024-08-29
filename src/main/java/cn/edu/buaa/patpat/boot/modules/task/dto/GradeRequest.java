/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GradeRequest {
    /**
     * List of Student ID to grade.
     */
    @NotNull
    private List<Integer> ids;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer score;
}
