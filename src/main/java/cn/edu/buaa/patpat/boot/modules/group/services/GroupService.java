package cn.edu.buaa.patpat.boot.modules.group.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.group.dto.CreateGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupFilterMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupMemberMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class GroupService extends BaseService {
    private final GroupMapper groupMapper;
    private final GroupMemberMapper groupMemberMapper;
    private final BucketApi bucketApi;
    private final GroupFilterMapper groupFilterMapper;

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

    public void quit(int groupId, int courseId, int accountId) {
        if (isLocked(groupId)) {
            throw new ForbiddenException(M("group.locked"));
        }
        groupMemberMapper.delete(courseId, accountId);
    }

    public void kick(int groupId, int courseId, int accountId) {
        if (isLocked(groupId)) {
            throw new ForbiddenException(M("group.locked"));
        }
        if (groupMemberMapper.find(courseId, accountId) == null) {
            throw new NotFoundException(M("group.member.exists.not"));
        }
        int updated = groupMemberMapper.delete(courseId, accountId);
        if (updated == 0) {
            throw new NotFoundException(M("group.member.exists.not"));
        }
    }

    public List<GroupListView> querySummarizedGroups(int courseId, GroupConfig config) {
        List<GroupListView> groups = groupFilterMapper.querySummarizedGroups(courseId);
        groups.forEach(group -> group.setMaxSize(config.getMaxSize()));
        return groups;
    }

    public List<GroupView> queryGroups(int courseId, GroupConfig config) {
        List<GroupView> groups = groupFilterMapper.queryGroups(courseId);
        List<GroupMemberView> members = groupFilterMapper.findMembersInCourse(courseId);
        Map<Integer, GroupView> groupMap = groups.stream()
                .collect(Collectors.toMap(GroupView::getId, group -> group));

        groups.forEach(group -> {
            group.setMaxSize(config.getMaxSize());
            group.setMembers(new ArrayList<>());
        });
        members.forEach(member -> {
            member.setAvatar(bucketApi.recordToUrl(member.getAvatar()));
            GroupView group = groupMap.get(member.getGroupId());
            if (group != null) {
                if (member.isOwner()) {
                    group.getMembers().add(0, member);
                } else {
                    group.getMembers().add(member);
                }
            }
        });

        return groups;
    }

    /**
     * Get all students who are not in any group.
     */
    public List<RogueStudentView> queryRogueStudents(int courseId) {
        return groupFilterMapper.queryRogueStudents(courseId);
    }

    private List<GroupMemberView> getMembersInGroup(int groupId) {
        var members = groupFilterMapper.findMembersInGroup(groupId);
        members.forEach(member -> member.setAvatar(
                bucketApi.recordToUrl(member.getAvatar())));
        return members;
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
