package cn.edu.buaa.patpat.boot.modules.task.models.mappers;

import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TaskScoreMapper {
    @Insert("""
            INSERT INTO `task_score` (
                `task_id`, `course_id`, `account_id`, `student_id`,
                `score`, `late`, `record`, `created_at`, `updated_at`
            ) VALUES (
                #{taskId}, #{courseId}, #{accountId}, #{studentId},
                #{score}, #{late}, #{record}, #{createdAt}, #{updatedAt}
            ) ON DUPLICATE KEY UPDATE
                `score` = GREATEST(`score`, VALUES(`score`)),
                `late` = VALUES(`late`),
                `record` = VALUES(`record`),
                `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrUpdate(TaskScore score);

    @Select("""
            SELECT * FROM `task_score`
            WHERE `task_id` = #{taskId} AND `course_id` = #{courseId} AND `account_id` = #{accountId}
            """)
    TaskScore find(int taskId, int courseId, int accountId);

    @Select("""
            SELECT * FROM `task_score`
            WHERE `task_id` = #{taskId} AND `student_id` = #{studentId}
            """)
    TaskScore findByTaskIdAndStudentId(int taskId, int studentId);

    @Update("""
            UPDATE `task_score`
            SET `score` = #{score}
            WHERE `task_id` = #{taskId} AND `student_id` IN (
                <foreach collection="studentIds" item="studentId" separator="," open="(" close=")">
                    #{studentId}
                </foreach>
            )
            """)
    int batchUpdateScore(int taskId, int score, List<Integer> studentIds);

    @Update("""
            UPDATE `task_score`
            SET `score` = #{score}
            WHERE `task_id` = #{taskId} AND `student_id` = #{studentId}
            """)
    int updateScore(int taskId, int score, int studentId);

    @Update("""
            UPDATE `task_score`
            SET `score` = #{score}
            WHERE `task_id` = #{taskId} AND `account_id` = #{accountId}
            """)
    int updateScoreByAccount(int taskId, int accountId, int score);
}

