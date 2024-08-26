package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskScoreIndexView implements Serializable {
    private int taskId;
    private int accountId;
    private int score;
    private boolean late;
}
