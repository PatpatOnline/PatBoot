/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.api;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupAssignmentService;
import cn.edu.buaa.patpat.boot.modules.group.services.impl.GroupAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupApi {
    private final GroupAssignmentService groupAssignmentService;
    private final GroupAdminService groupAdminService;

    public GroupAssignment findGroupAssignment(int courseId) {
        return groupAssignmentService.find(courseId);
    }

    public List<GroupView> queryGroups(int courseId) {
        return groupAdminService.queryGroups(courseId);
    }

    public List<GroupScoreListView> queryGroupScores(int courseId) {
        return groupAdminService.queryScores(courseId);
    }
}
