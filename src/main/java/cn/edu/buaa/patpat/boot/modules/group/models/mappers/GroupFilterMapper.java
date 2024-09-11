/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import org.apache.ibatis.annotations.CacheNamespaceRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@CacheNamespaceRef(GroupMapper.class)
public interface GroupFilterMapper {
    @Select("""
            SELECT `account_id`, `owner`, `weight`, `group_id`
            FROM `group_member` WHERE `group_id` = #{groupId}
            ORDER BY `owner` DESC
            """)
    @Options(useCache = false)
    List<GroupMemberView> findMemberViewsInGroup(int groupId);

    @Select("""
            SELECT `account_id`, `owner`, `weight`, `group_id`
            FROM `group_member`
            ORDER BY `owner` DESC
            """)
    @Options(useCache = false)
    List<GroupMemberView> findMemberViewsInCourse(int courseId);

    @Select("""
            SELECT `id`, `name`, `description`, `locked`
            FROM `group`
            WHERE `id` = #{id}
            """)
    GroupView findGroup(int id);

    @Select("""
            SELECT COUNT(*)
            FROM `group_member`
            WHERE `group_id` = #{groupId}
            """)
    int countMembersInGroup(int groupId);

    @Select("""
            SELECT  `id`, `name`, `description`, `locked`
            FROM `group`
            WHERE `course_id` = #{courseId}
            """)
    List<GroupView> queryGroups(int courseId);

    @Select("""
            SELECT `s`.account_id, `a`.buaa_id, `a`.name, `s`.teacher_id
            FROM (
                SELECT `account_id`, `teacher_id` FROM `student`
                WHERE course_id = #{courseId}
            ) AS `s` LEFT JOIN (
                SELECT `id`, `buaa_id`, `name` FROM `account`
            ) AS `a` ON `s`.`account_id` = `a`.`id` LEFT JOIN (
                SELECT `account_id` FROM `group_member`
                WHERE `course_id` = #{courseId}
            ) AS `m` ON `s`.`account_id` = `m`.`account_id`
            WHERE `m`.`account_id` IS NULL
            """)
    List<RogueStudentView> queryRogueStudents(int courseId);
}
