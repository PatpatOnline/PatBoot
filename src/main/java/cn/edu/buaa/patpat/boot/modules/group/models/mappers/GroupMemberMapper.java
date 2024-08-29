/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespaceRef(GroupMapper.class)
public interface GroupMemberMapper {
    @Insert("""
            INSERT INTO `group_member` (`group_id`, `course_id`, `account_id`, `owner`, `weight`)
            VALUES (#{groupId}, #{courseId}, #{accountId}, #{owner}, #{weight})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(GroupMember member);

    @Update("""
            UPDATE `group_member`
            SET `owner` = #{owner}, `weight` = #{weight}
            WHERE `course_id` = #{courseId} AND `account_id` = #{accountId}
            """)
    void update(GroupMember member);

    @Delete("DELETE FROM `group_member` WHERE `course_id` = #{courseId} AND `account_id` = #{accountId}")
    int delete(int courseId, int accountId);

    @Delete("DELETE FROM `group_member` WHERE `group_id` = #{groupId}")
    void deleteByGroup(int groupId);

    @Select("SELECT * FROM `group_member` WHERE `course_id` = #{courseId} AND `account_id` = #{accountId}")
    GroupMember find(int courseId, int accountId);
}
