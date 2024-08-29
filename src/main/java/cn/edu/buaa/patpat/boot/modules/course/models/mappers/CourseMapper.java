/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.CourseTutorial;
import cn.edu.buaa.patpat.boot.modules.course.models.views.CourseView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace
public interface CourseMapper {
    @Insert("""
            INSERT INTO `course` (
                `name`, `code`, `semester`, `active`, `created_at`, `updated_at`
            )
            VALUES (
                #{name}, #{code}, #{semester}, #{active}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Course course);

    @Select("SELECT * FROM `course` WHERE `id` = #{id}")
    Course find(int id);

    @Select("""
            SELECT * FROM `course`
            WHERE `id` = (
                SELECT `course_id` FROM `student`
                WHERE `course_id` = #{id} AND `account_id` = #{accountId}
            ) AND `active` = true
            """)
    Course findActive(int id, int accountId);

    @Select("""
            SELECT * FROM `course`
            WHERE `active` = true AND `id` IN (
                SELECT `course_id` FROM `student`
                WHERE `account_id` = #{accountId}
            )
            """)
    List<Course> findAllActive(int accountId);

    @Delete("DELETE FROM `course` WHERE `id` = #{id}")
    void delete(int id);

    @Update("""
            UPDATE `course`
            SET `name` = #{name},
                `code` = #{code},
                `semester` = #{semester},
                `active` = #{active},
                `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Course course);

    @Select("SELECT * FROM `course` ORDER BY `semester` DESC")
    List<Course> getAll();

    @Select("""
            SELECT `id`, `name`, `code`, `semester`, `active`, `url` AS `tutorial`
            FROM `course` LEFT JOIN `course_tutorial` ON `course`.`id` = `course_tutorial`.`course_id`
            WHERE `id` = #{id}
            """)
    CourseView findView(int id);

    @Insert("""
            INSERT INTO `course_tutorial` (`course_id`, `url`, `created_at`, `updated_at`)
            VALUES (#{courseId}, #{url}, #{createdAt}, #{updatedAt})
            ON DUPLICATE KEY UPDATE `url` = VALUES(`url`), `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrUpdateTutorial(CourseTutorial tutorial);

    @Delete("DELETE FROM `course_tutorial` WHERE `course_id` = #{courseId}")
    int deleteTutorial(int courseId);

    @Select("SELECT * FROM `course_tutorial` WHERE `course_id` = #{courseId}")
    CourseTutorial findTutorial(int courseId);

    @Select("SELECT `id`, `name` FROM `course` WHERE `id` = #{id}")
    Course findName(int id);
}
