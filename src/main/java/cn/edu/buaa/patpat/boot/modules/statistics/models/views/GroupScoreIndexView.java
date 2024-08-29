/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupScoreIndexView implements Serializable {
    private int accountId;
    private int weight;

    /**
     * The original score of the group.
     */
    private int groupScore;

    /**
     * The individual score, groupScore * (weight / 100).
     */
    private int score;
}
