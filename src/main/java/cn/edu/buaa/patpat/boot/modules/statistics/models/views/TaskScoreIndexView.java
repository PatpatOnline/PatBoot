package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

@Data
public class TaskScoreIndexView {
    private int taskId;
    private int accountId;
    private int score;
    private boolean late;
}
