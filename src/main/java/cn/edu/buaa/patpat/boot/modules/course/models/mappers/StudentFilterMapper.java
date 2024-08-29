/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentDetailView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentListView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface StudentFilterMapper {
    @Select("SELECT * FROM `student` WHERE `id` = #{id} LIMIT 1")
    Student findById(int id);

    @Select("""
            SELECT * FROM `student`
            WHERE `account_id` = #{accountId} AND `course_id` = #{courseId}
            LIMIT 1
            """)
    Student findByAccountAndCourse(int accountId, int courseId);

    @Select("""
            SELECT
                `stu`.`id`,
                `stu`.`account_id`,
                `as`.`buaa_id`,
                `as`.`name` AS `student_name`,
                `at`.`name` AS `teacher_name`,
                `as`.`school`,
                `stu`.`major`,
                `stu`.`class_name`,
                `stu`.`repeat`,
                `as`.`avatar`,
                `stu`.`created_at`
            FROM (
                SELECT * FROM `student`
                WHERE `account_id` = #{accountId} AND `course_id` = #{courseId}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name`, `school`, `avatar` FROM `account`
            ) AS `as` ON `stu`.account_id = `as`.`id` JOIN (
                SELECT `id`, `name` FROM `account`
            ) AS `at` ON `stu`.teacher_id = `at`.`id`
            """)
    StudentDetailView findDetailViewByAccountAndCourse(int accountId, int courseId);

    @Select("""
            SELECT
                `stu`.`id`,
                `stu`.`account_id`,
                `as`.`buaa_id`,
                `as`.`name` AS `student_name`,
                `at`.`name` AS `teacher_name`,
                `as`.`school`,
                `stu`.`major`,
                `stu`.`class_name`,
                `stu`.`repeat`,
                `as`.`avatar`,
                `stu`.`created_at`
            FROM (
                SELECT * FROM `student`
                WHERE `id` = #{id}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name`, `school`, `avatar` FROM `account`
            ) AS `as` ON `stu`.account_id = `as`.`id` JOIN (
                SELECT `id`, `name` FROM `account`
            ) AS `at` ON `stu`.teacher_id = `at`.`id`
            """)
    StudentDetailView findDetailViewById(int id);

    @SelectProvider(type = MapperProvider.class, method = "count")
    int count(int courseId, StudentFilter filter);

    @Select("""
            SELECT
                `stu`.`id`,
                `stu`.`account_id`,
                `as`.`buaa_id`,
                `as`.`name` AS `student_name`,
                `stu`.`teacher_id`,
                `as`.`school`,
                `stu`.`major`,
                `stu`.`class_name`,
                `stu`.`repeat`,
                `stu`.`created_at`
            FROM (
                SELECT * FROM `student`
                WHERE `id` = #{id}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name`, `school` FROM `account`
            ) AS `as` ON `stu`.account_id = `as`.`id`
            """)
    StudentListView queryById(int id);

    @SelectProvider(type = MapperProvider.class, method = "query")
    List<StudentListView> queryImpl(int courseId, int pageSize, int offset, StudentFilter filter);

    default List<StudentListView> query(int courseId, int page, int pageSize, StudentFilter filter) {
        return queryImpl(courseId, pageSize, (page - 1) * pageSize, filter);
    }
}
