/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.services.impl;

import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.entities.ScoreConfig;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.GroupScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.StudentIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * All information required to export score.
 */
@Getter
public class ExportScoreRequest {
    /**
     * taskId -> (accountId -> score)
     */
    private final Map<Integer, Map<Integer, TaskScoreIndexView>> taskScores = new HashMap<>();
    /**
     * accountId -> total lab score
     */
    private final Map<Integer, Integer> labScores = new HashMap<>();
    /**
     * accountId -> total iter score
     */
    private final Map<Integer, Integer> iterScores = new HashMap<>();
    @Setter
    private int courseId;
    @Setter
    private String courseName;
    @Setter
    private ScoreConfig config;
    @Setter
    private LocalDateTime timestamp;
    @Setter
    private List<TeacherIndexView> teachers;
    @Setter
    private List<StudentIndexView> students;
    private List<TaskIndexView> tasks;
    /**
     * accountId -> score
     */
    private Map<Integer, GroupScoreIndexView> groupScores;
    private int totalLabs;
    private int totalIters;

    public void setTasks(List<TaskIndexView> tasks) {
        this.tasks = tasks;
        totalLabs = (int) tasks.stream().filter(t -> t.getType() == TaskTypes.LAB).count();
        totalIters = tasks.size() - totalLabs;
    }

    /**
     * Add a list of task scores.
     * If the task scores already exist, it will NOT be replaced.
     */
    public void addTaskScores(int taskId, int type, List<TaskScoreIndexView> scores) {
        Map<Integer, Integer> map = type == TaskTypes.LAB ? labScores : iterScores;
        for (var score : scores) {
            map.compute(score.getAccountId(), (id, accumulate) -> {
                if (accumulate == null) {
                    accumulate = 0;
                }
                accumulate += config.getScore(score.getScore(), score.isLate());
                return accumulate;
            });
        }

        taskScores.computeIfAbsent(taskId, id -> scores.stream()
                .collect(Collectors.toMap(TaskScoreIndexView::getAccountId, s -> s)));
    }

    /**
     * Set a list of task scores.
     * The existing task scores will be replaced.
     */
    public void setGroupScores(List<GroupScoreIndexView> scores) {
        groupScores = scores == null ? Map.of() : scores.stream()
                .collect(Collectors.toMap(GroupScoreIndexView::getAccountId, s -> s));
    }
}
