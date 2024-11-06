/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScoreConfig implements Serializable {
    private int courseId;
    private int labScore;
    private int iterScore;
    private int projScore;

    /**
     * The percentage of the score for late submission.
     * Final score = score * (latePercent / 100)
     */
    private int latePercent;

    public int getTotalScore() {
        return labScore + iterScore + projScore;
    }

    public int getScore(int score, boolean isLate) {
        return isLate ? ((int) (score * ((double) latePercent / 100.0))) : score;
    }
}
