/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateScoreConfigRequest {
    @Max(100)
    @Min(0)
    private Integer labScore;

    @Max(100)
    @Min(0)
    private Integer iterScore;

    @Max(100)
    @Min(0)
    private Integer projScore;

    @Max(100)
    @Min(0)
    private Integer latePercent;
}
