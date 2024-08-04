package cn.edu.buaa.patpat.boot.modules.task.models.mappers;

import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TaskMapper {
    @Insert("""
            INSERT INTO `task` (
                `course_id`, `type`, `title`, `content`, `visible`,
                `start_time`, `deadline_time`, `end_time`, `created_at`, `updated_at`
            ) VALUES (
                #{courseId}, #{type}, #{title}, #{content}, #{visible},
                #{startTime}, #{deadlineTime}, #{endTime}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Task task);

    @Select("""
            SELECT `id`, `course_id`, `title`, `content`, `visible`,
                   `start_time`, `deadline_time`, `end_time`
            FROM `task`
            WHERE `id` = #{id} AND `course_id` = #{courseId} AND `type` = #{type}
            """)
    Task findUpdate(int id, int courseId, int type);

    @Update("""
            UPDATE `task`
            SET
                `title` = #{title},
                `content` = #{content},
                `visible` = #{visible},
                `start_time` = #{startTime},
                `deadline_time` = #{deadlineTime},
                `end_time` = #{endTime},
                `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Task task);

    @Update("""
            DELETE FROM `task`
            WHERE `id` = #{id} AND `course_id` = #{courseId} AND `type` = #{type}
            """)
    int delete(int id, int courseId, int type);
}
