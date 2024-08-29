/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.dto;

import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JudgeResponseDto extends JudgeTimestamp {
    private int id;
    private int problemId;
    private String language;

    private int score;
    private TestResult result;

    private JudgePayload payload;
}
