/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.account.api.AccountApi;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountBadgeView;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupFilterMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupMemberMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

public abstract class GroupBaseService extends BaseService {
    @Autowired
    protected GroupMapper groupMapper;
    @Autowired
    protected GroupFilterMapper groupFilterMapper;
    @Autowired
    protected GroupMemberMapper groupMemberMapper;
    @Autowired
    private AccountApi accountApi;

    public Group getGroup(int id) {
        Group group = groupMapper.find(id);
        if (group == null) {
            throw new NotFoundException(M("group.exists.not"));
        }
        return group;
    }

    public GroupView getGroup(int id, GroupConfig config) {
        GroupView view = groupFilterMapper.findGroup(id);
        if (view == null) {
            throw new NotFoundException(M("group.exists.not"));
        }
        view.setMembers(getMembersInGroup(id));
        view.setMaxSize(config.getMaxSize());
        return view;
    }

    public GroupMember findMember(int courseId, int accountId) {
        return groupMemberMapper.find(courseId, accountId);
    }

    public List<GroupMemberView> getMembersInGroup(int groupId) {
        var members = groupFilterMapper.findMemberViewsInGroup(groupId);
        mendMembers(members);
        return members;
    }

    /**
     * This method ensures that the group leader is always the first member in the group.
     */
    public List<GroupView> queryGroups(int courseId, GroupConfig config) {
        List<GroupView> groups = groupFilterMapper.queryGroups(courseId);
        Map<Integer, GroupView> groupMap = groups.stream().collect(Collectors.toMap(GroupView::getId, group -> group));
        groups.forEach(group -> {
            group.setMaxSize(config.getMaxSize());
            group.setMembers(new ArrayList<>());
        });

        List<GroupMemberView> members = groupFilterMapper.findMemberViewsInCourse(courseId);
        mendMembers(members);
        members.forEach(member -> Optional.ofNullable(groupMap.get(member.getGroupId()))
                .ifPresent(value -> value.getMembers().add(member)));

        return groups;
    }

    private void mendMembers(List<GroupMemberView> members) {
        var badgeMap = accountApi.queryBadges(members.stream()
                .map(GroupMemberView::getAccountId)
                .toList()).stream().collect(
                Collectors.toMap(AccountBadgeView::getId, badge -> badge));
        members.forEach(member -> Optional.ofNullable(badgeMap.get(member.getAccountId()))
                .ifPresent(value -> mappers.map(value, member)));
    }
}
