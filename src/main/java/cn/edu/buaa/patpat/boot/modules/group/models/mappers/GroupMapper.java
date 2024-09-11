/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupInfoView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace
public interface GroupMapper {
    @Insert("""
            INSERT INTO `group` (`course_id`, `name`, `description`, `locked`)
            VALUES (#{courseId}, #{name}, #{description}, #{locked})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Group group);

    @Update("""
            UPDATE `group`
            SET `name` = #{name}, `description` = #{description}, `locked` = #{locked}
            WHERE `id` = #{id}
            """)
    void update(Group group);

    @Delete("DELETE FROM `group` WHERE `id` = #{id} AND `course_id` = #{courseId}")
    void delete(int id, int courseId);

    @Select("SELECT * FROM `group` WHERE `id` = #{id}")
    Group find(int id);

    @Select("SELECT COUNT(*) FROM `group` WHERE `name` = #{name} AND `course_id` = #{courseId}")
    boolean existsByNameAndCourseId(String name, int courseId);

    @Select("SELECT `locked` FROM `group` WHERE `id` = #{id}")
    Group findLocked(int id);

    @Select("SELECT `id`, `name` FROM `group` WHERE `course_id` = #{courseId}")
    List<GroupInfoView> getGroups(int courseId);
}
