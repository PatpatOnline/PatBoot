package cn.edu.buaa.patpat.boot.modules.task.models.mappers;

import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskProblemListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskProblemView;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskProblemMapper {
    @Delete("DELETE FROM `task_problem` WHERE `task_id` = #{taskId}")
    void batchDelete(int taskId);

    @Insert("""
            <script>
            INSERT IGNORE INTO `task_problem` (`task_id`, `problem_id`, `order`)
            VALUES
            <foreach collection="problemIds" item="problemId" index="index" separator=",">
                (#{taskId}, #{problemId}, #{index})
            </foreach>
            </script>
            """)
    void batchAdd(int taskId, List<Integer> problemIds);

    @Insert("""
            INSERT IGNORE INTO `task_problem` (`task_id`, `problem_id`, `order`)
            VALUES (#{taskId}, #{problemId}, 0)
            """)
    void add(int taskId, int problemId);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM `problem`
            WHERE `id` IN
            <foreach collection="problemIds" item="problemId" open="(" separator="," close=")">
                #{problemId}
            </foreach>
            </script>
            """)
    int count(List<Integer> problemIds);

    @Select("""
            SELECT `problem_id`, `title`
            FROM `task_problem` JOIN `problem` ON `problem`.`id` = `task_problem`.`problem_id`
            WHERE `task_id` = #{taskId}
            ORDER BY `order`
            """)
    List<TaskProblemListView> findTaskProblemList(int taskId);

    @Select("""
            SELECT t.`problem_id`, p.`title`, ps.`score`
            FROM (
                SELECT * FROM `task_problem`
                WHERE `task_id` = #{taskId}
            ) AS `t` JOIN (
                SELECT `id`, `title` FROM `problem`
            ) AS `p` ON `p`.`id` = `t`.`problem_id` LEFT JOIN (
                SELECT * FROM `problem_score`
                WHERE `account_id` = #{accountId}
            ) AS `ps` ON `ps`.`problem_id` = `t`.`problem_id`
            ORDER BY `order`
            """)
    List<TaskProblemView> findTaskProblems(int taskId, int accountId);
}
