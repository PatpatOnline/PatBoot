/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import lombok.Data;

@Data
public class SubmissionFilter {
    private Integer id;
    private String buaaId;
    private String name;
    private Integer problemId;
    private Integer minScore;
    private Integer maxScore;

    public SubmissionFilter(Integer id, String buaaId, String name, Integer problemId, Integer minScore, Integer maxScore) {
        this.id = id;
        this.buaaId = buaaId;
        this.name = name;
        this.problemId = problemId;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }
}
