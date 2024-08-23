package cn.edu.buaa.patpat.boot.modules.statistics.models.mappers;

import cn.edu.buaa.patpat.boot.modules.statistics.models.views.GroupScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.StudentIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.TaskScoreIndexView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    @Select("""
            SELECT `account_id`, `buaa_id`, `name`, `teacher_id`
            FROM (
                SELECT `account_id`, `teacher_id` FROM student
                WHERE course_id = #{courseId}
            ) AS `stu` LEFT JOIN (
                SELECT `id`, `buaa_id`, `name` FROM account
            ) AS `acc` ON `stu`.account_id = `acc`.id
            """)
    List<StudentIndexView> queryStudents(int courseId);

    @Select("""
            SELECT `id`, `type`, `title`
            FROM `task`
            WHERE `course_id` = #{courseId} AND `visible` = TRUE
            ORDER BY `type`, `title`
            """)
    List<TaskIndexView> queryTasks(int courseId);

    @Select("""
            SELECT `task_id`, `account_id`, `score`, `late`
            FROM `task_score`
            WHERE `task_id` = #{taskId} AND `student_id` <> 0
            """)
    List<TaskScoreIndexView> queryTaskScores(int taskId);

    @Select("""
            SELECT `group_id`, `score`
            FROM `group_score`
            WHERE `course_id` = #{courseId}
            """)
    List<GroupScoreIndexView> queryGroupScores(int courseId);
}
