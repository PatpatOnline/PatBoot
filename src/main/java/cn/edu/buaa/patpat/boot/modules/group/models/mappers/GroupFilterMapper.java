package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupListView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import org.apache.ibatis.annotations.CacheNamespaceRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@CacheNamespaceRef(GroupMapper.class)
public interface GroupFilterMapper {
    @Select("""
            SELECT `m`.`account_id`, `m`.`owner`, `m`.`weight`, `m`.`group_id`,
                `a`.`buaa_id`, `a`.`name`, `a`.`avatar`
            FROM (
                SELECT * FROM `group_member` WHERE `group_id` = #{groupId}
            ) AS m LEFT JOIN (
                SELECT `id`, `buaa_id`, `name`, `avatar`
                FROM `account`
            ) AS a ON m.`account_id` = a.`id`
            ORDER BY `m`.`owner` DESC
            """)
    List<GroupMemberView> findMemberViewsInGroup(int groupId);

    @Select("""
            SELECT `m`.`account_id`, `m`.`owner`, `m`.`weight`, `m`.`group_id`,
                `a`.`buaa_id`, `a`.`name`, `a`.`avatar`
            FROM (
                SELECT * FROM `group_member` WHERE `course_id` = #{courseId}
            ) AS m LEFT JOIN (
                SELECT `id`, `buaa_id`, `name`, `avatar`
                FROM `account`
            ) AS a ON m.`account_id` = a.`id`
            ORDER BY `m`.`owner` DESC
            """)
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
            SELECT `g`.`id`, `g`.`name`, `g`.`description`, `g`.`locked`,
                (SELECT COUNT(*) FROM `group_member` WHERE `group_id` = `g`.`id`) AS `member_count`,
                `a`.`buaa_id` AS `owner_buaa_id`, `a`.`name` AS `owner_name`
            FROM (
                SELECT * FROM `group` WHERE `course_id` = #{courseId}
            ) AS g LEFT JOIN (
                SELECT `account_id`, `group_id` FROM `group_member`
                WHERE `owner` = TRUE
            ) AS m ON g.`id` = m.`group_id` LEFT JOIN (
                SELECT `id`, `buaa_id`, `name` FROM `account`
            ) AS a ON m.`account_id` = a.`id`
            """)
    List<GroupListView> querySummarizedGroups(int courseId);

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
