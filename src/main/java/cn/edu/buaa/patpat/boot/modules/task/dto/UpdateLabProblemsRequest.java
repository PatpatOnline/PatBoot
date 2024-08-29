/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateLabProblemsRequest {
    /**
     * If list is empty, all problems will be removed.
     */
    @NotNull
    private List<Integer> problemIds;
}
