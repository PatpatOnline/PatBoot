package cn.edu.buaa.patpat.boot.modules.task.models.mappers;

import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskScoreView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskStudentView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskStatisticMapper {
    @Select("""
            SELECT `stu`.`id` AS `studentId`, `acc`.`id` AS `accountId`, `acc`.`buaa_id`, `acc`.`name`
            FROM (
                SELECT `id`, `account_id` FROM `student`
                WHERE `course_id` = #{courseId} AND `teacher_id` = #{teacherId}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name` FROM `account`
            ) AS `acc` ON `stu`.`account_id` = `acc`.`id`
            """)
    List<TaskStudentView> getStudentsByTeacher(int courseId, int teacherId);

    @Select("""
            SELECT `stu`.`id` AS `studentId`, `acc`.`id` AS `accountId`, `acc`.`buaa_id`, `acc`.`name`
            FROM (
                SELECT `id`, `account_id` FROM `student`
                WHERE `course_id` = #{courseId}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name` FROM `account`
            ) AS `acc` ON `stu`.`account_id` = `acc`.`id`
            """)
    List<TaskStudentView> getStudents(int courseId);

    @Select("""
            SELECT `task_id`, `student_id`, `score`, `late`
            FROM `task_score`
            WHERE `task_id` = #{taskId}
            """)
    List<TaskScoreView> getScores(int taskId);
}
