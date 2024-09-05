/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.services;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.modules.account.api.AccountApi;
import cn.edu.buaa.patpat.boot.modules.course.api.CourseApi;
import cn.edu.buaa.patpat.boot.modules.group.api.GroupApi;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.mappers.StatisticsMapper;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.GroupScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.StudentIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.services.impl.ExportScoreRequest;
import cn.edu.buaa.patpat.boot.modules.statistics.services.impl.ScoreExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService extends BaseService {
    private final StatisticsMapper statisticsMapper;
    private final GroupApi groupApi;
    private final ScoreConfigService scoreConfigService;
    private final AccountApi accountApi;
    private final ScoreExporter scoreExporter;
    private final CourseApi courseApi;

    public List<StudentIndexView> queryStudents(int courseId) {
        return statisticsMapper.queryStudents(courseId);
    }

    public List<TaskIndexView> queryTasks(int courseId) {
        return statisticsMapper.queryTasks(courseId);
    }

    public List<TaskScoreIndexView> queryTaskScores(int taskId) {
        return statisticsMapper.queryTaskScores(taskId);
    }

    /**
     * Return null if group assignment is not published or visible.
     */
    public List<GroupScoreIndexView> queryGroupScores(int courseId) {
        GroupAssignment assignment = groupApi.findGroupAssignment(courseId);
        if ((assignment == null) || (!assignment.isVisible())) {
            return null;
        }
        List<GroupView> groups = groupApi.queryGroups(courseId);
        List<GroupScoreListView> groupScores = groupApi.queryGroupScores(courseId);
        List<GroupScoreIndexView> views = new ArrayList<>();
        for (var group : groups) {
            int score = groupScores.stream().filter(s -> s.getGroupId() == group.getId()).findFirst().map(GroupScoreListView::getScore).orElse(Globals.NOT_SUBMITTED);
            // only add group score if it is submitted
            if (score != Globals.NOT_SUBMITTED) {
                addGroupScoreIndexView(views, group, score);
            }
        }
        return views;
    }

    public Resource export(int courseId) {
        var request = new ExportScoreRequest();
        request.setCourseId(courseId);
        request.setCourseName(courseApi.getCourseName(courseId));
        request.setConfig(scoreConfigService.get(courseId));
        request.setTimestamp(LocalDateTime.now());

        request.setTeachers(accountApi.queryTeachers());
        request.setStudents(queryStudents(courseId));

        var tasks = queryTasks(courseId);
        request.setTasks(tasks);
        for (var task : tasks) {
            request.addTaskScores(task.getId(), task.getType(), queryTaskScores(task.getId()));
        }

        request.setGroupScores(queryGroupScores(courseId));

        return scoreExporter.export(request);
    }

    private void addGroupScoreIndexView(List<GroupScoreIndexView> groupScoreIndexViews, GroupView group, int groupScore) {
        int totalWeight = 0;
        int memberCount = group.getMembers().size();

        for (var member : group.getMembers()) {
            totalWeight += member.getWeight();
        }

        for (var member : group.getMembers()) {
            GroupScoreIndexView view = new GroupScoreIndexView();
            view.setAccountId(member.getAccountId());
            view.setWeight(member.getWeight());
            view.setGroupScore(groupScore);
            if (groupScore > 0) {
                // cannot exceed 100
                view.setScore(Math.min(Globals.FULL_SCORE, (int) ((double) groupScore * memberCount * member.getWeight() / totalWeight)));
            } else {
                view.setScore(groupScore);
            }
            groupScoreIndexViews.add(view);
        }
    }
}
