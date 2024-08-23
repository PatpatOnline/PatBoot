package cn.edu.buaa.patpat.boot.modules.group.services.impl;

import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.course.api.CourseApi;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupScore;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupScoreMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class GroupAdminService extends GroupBaseService {
    private final GroupScoreMapper groupScoreMapper;
    private final CourseApi courseApi;
    private final GroupExporter groupExporter;

    /**
     * This method ensures that the group leader is always the first member in the group.
     */
    public List<GroupView> queryGroups(int courseId, GroupConfig config) {
        List<GroupView> groups = groupFilterMapper.queryGroups(courseId);
        List<GroupMemberView> members = groupFilterMapper.findMemberViewsInCourse(courseId);
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
     * This overload will use a pseudo {@link GroupConfig} with default values,
     * which is suitable for internal use to export groups.
     * In this case, {@link GroupView#getMaxSize()} will be invalid.
     */
    public List<GroupView> queryGroups(int courseId) {
        return queryGroups(courseId, new GroupConfig());
    }


    /**
     * Get all students who are not in any group.
     */
    public List<RogueStudentView> queryRogueStudents(int courseId) {
        return groupFilterMapper.queryRogueStudents(courseId);
    }

    public GroupScore score(int groupId, int score) {
        GroupScore groupScore = groupScoreMapper.find(groupId);
        if (groupScore == null) {
            throw new NotFoundException(M("group.assignment.submit.not"));
        }
        groupScore.setScore(score);
        groupScoreMapper.updateScore(groupId, score);

        return groupScore;
    }

    /**
     * Query scores of all groups in the course.
     * This should be used together with {@link #queryGroups(int, GroupConfig)}.
     */
    public List<GroupScoreListView> queryScores(int courseId) {
        return groupScoreMapper.findInCourse(courseId);
    }

    public Resource exportGroups(int courseId, GroupConfig config) {
        String courseName = courseApi.getCourseName(courseId);
        List<GroupView> groups = queryGroups(courseId, config);
        List<RogueStudentView> rogueStudents = queryRogueStudents(courseId);

        return groupExporter.exportGroups(
                courseName,
                groups,
                rogueStudents,
                config);
    }
}
