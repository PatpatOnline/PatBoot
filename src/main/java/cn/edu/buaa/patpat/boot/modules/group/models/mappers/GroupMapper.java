package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import org.apache.ibatis.annotations.*;

@Mapper
public interface GroupMapper {
    @Insert("""
            INSERT INTO `group` (`course_id`, `name`, `description`, `locked`)
            VALUES (#{courseId}, #{name}, #{description}, #{locked})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Group group);

    @Update("""
            UPDATE `group`
            SET `name` = #{name}, `description` = #{description}, `locked` = #{locked}
            WHERE `id` = #{id}
            """)
    int update(Group group);

    @Delete("DELETE FROM `group` WHERE `id` = #{id} AND `course_id` = #{courseId}")
    int delete(int id, int courseId);

    @Select("SELECT * FROM `group` WHERE `id` = #{id}")
    Group find(int id);

    @Select("SELECT COUNT(*) FROM `group` WHERE `name` = #{name} AND `course_id` = #{courseId}")
    boolean existsByNameAndCourseId(String name, int courseId);

    @Select("SELECT `locked` FROM `group` WHERE `id` = #{id}")
    Group findLocked(int id);
}
