package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

@Data
public class GroupScoreIndexView {
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
