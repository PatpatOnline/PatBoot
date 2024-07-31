package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentDetailView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentFilterMapper {
    @Select("""
            SELECT * FROM `student`
            WHERE `account_id` = #{accountId} AND `course_id` = #{courseId}
            LIMIT 1
            """)
    Student find(int accountId, int courseId);

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
}
