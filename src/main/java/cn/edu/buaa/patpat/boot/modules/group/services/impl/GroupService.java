/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.services.impl;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.group.dto.CreateGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupScoreMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class GroupService extends GroupBaseService {
    private final GroupScoreMapper groupScoreMapper;

    @Transactional
    public Group create(int courseId, int accountId, CreateGroupRequest request) {
        if (groupMapper.existsByNameAndCourseId(request.getName(), courseId)) {
            throw new BadRequestException(M("group.name.exists"));
        }

        Group group = mappers.map(request, Group.class);
        group.setCourseId(courseId);
        groupMapper.save(group);
        addOwner(courseId, group.getId(), accountId);

        return group;
    }

    public Group update(Group group, UpdateGroupRequest request) {
        mappers.map(request, group);
        groupMapper.update(group);
        return group;
    }

    @Transactional
    public void dismiss(int groupId, int courseId) {
        groupMapper.delete(groupId, courseId);
        groupMemberMapper.deleteByGroup(groupId);
    }

    public void join(int groupId, int courseId, int accountId, GroupConfig config) {
        if (isLocked(groupId)) {
            throw new ForbiddenException(M("group.locked"));
        }
        int memberCount = groupFilterMapper.countMembersInGroup(groupId);
        if (memberCount >= config.getMaxSize()) {
            throw new ForbiddenException(M("group.join.full"));
        }
        addMember(courseId, groupId, accountId);
    }

    /**
     * Check if the group is locked outside.
     */
    public void quit(int courseId, int accountId) {
        if (groupMemberMapper.delete(courseId, accountId) == 0) {
            throw new NotFoundException(M("group.member.exists.not"));
        }
    }

    /**
     * Check if the group is locked outside.
     */
    public void kick(int courseId, int groupId, int accountId) {
        var member = getMember(courseId, groupId, accountId);
        if (member.getGroupId() != groupId) {
            throw new NotFoundException(M("group.member.exists.not"));
        }
        if (member.isOwner()) {
            // Shouldn't reach here, since only the owner can kick members and cannot kick himself.
            throw new ForbiddenException(M("group.kick.owner"));
        }
        groupMemberMapper.delete(courseId, accountId);
    }

    public void updateWeight(int courseId, int groupId, int accountId, int weight) {
        var member = getMember(courseId, groupId, accountId);
        member.setWeight(weight);
        groupMemberMapper.update(member);
    }

    public GroupScoreListView findScore(int groupId) {
        var score = groupScoreMapper.findById(groupId);
        if (score != null) {
            score.initFilename();
        }
        return score;
    }

    private GroupMember getMember(int courseId, int groupId, int accountId) {
        var member = groupMemberMapper.find(courseId, accountId);
        if (member == null || member.getGroupId() != groupId) {
            throw new NotFoundException(M("group.member.exists.not"));
        }
        return member;
    }

    private void addOwner(int courseId, int groupId, int accountId) {
        add(courseId, groupId, accountId, true);
    }

    private void addMember(int courseId, int groupId, int accountId) {
        add(courseId, groupId, accountId, false);
    }

    private void add(int courseId, int groupId, int accountId, boolean owner) {
        GroupMember member = new GroupMember();
        member.setCourseId(courseId);
        member.setGroupId(groupId);
        member.setAccountId(accountId);
        member.setOwner(owner);
        member.setWeight(100);
        groupMemberMapper.save(member);
    }

    private boolean isLocked(int groupId) {
        Group group = groupMapper.findLocked(groupId);
        if (group == null) {
            throw new NotFoundException(M("group.exists.not"));
        }
        return group.isLocked();
    }
}
