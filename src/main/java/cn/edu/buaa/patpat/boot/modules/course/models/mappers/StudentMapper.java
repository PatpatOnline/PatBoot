/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentInfoView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Insert("""
            INSERT INTO `student` (
                `account_id`, `course_id`, `teacher_id`, `major`, `class_name`, `repeat`, `created_at`
            ) VALUES (
                #{accountId}, #{courseId}, #{teacherId}, #{major}, #{className}, #{repeat}, #{createdAt}
            )
            """)
    void save(Student student);

    @Delete("DELETE FROM `student` WHERE `id` = #{id}")
    void delete(int id);

    @Update("""
            UPDATE `student`
            SET
                `account_id` = #{accountId},
                `course_id` = #{courseId},
                `teacher_id` = #{teacherId},
                `major` = #{major},
                `class_name` = #{className},
                `repeat` = #{repeat}
            WHERE `id` = #{id}
            """)
    void importUpdate(Student student);

    @Update("""
            UPDATE `student`
            SET
                `teacher_id` = #{teacherId},
                `major` = #{major},
                `class_name` = #{className},
                `repeat` = #{repeat}
            WHERE `id` = #{id}
            """)
    void update(Student student);

    @Select("""
            SELECT `stu`.`id` AS `studentId`, `acc`.`id` AS `accountId`, `acc`.`buaa_id`, `acc`.`name`
            FROM (
                SELECT `id`, `account_id` FROM `student`
                WHERE `course_id` = #{courseId} AND `teacher_id` = #{teacherId}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name` FROM `account`
            ) AS `acc` ON `stu`.`account_id` = `acc`.`id`
            """)
    List<StudentInfoView> getStudentsByTeacher(int courseId, int teacherId);
}
