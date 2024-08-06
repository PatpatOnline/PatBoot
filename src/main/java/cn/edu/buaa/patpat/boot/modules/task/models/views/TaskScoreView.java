package cn.edu.buaa.patpat.boot.modules.task.models.views;

import lombok.Data;

@Data
public class TaskScoreView {
    private int taskId;
    private int studentId;
    private int score;
    private boolean late;
}
