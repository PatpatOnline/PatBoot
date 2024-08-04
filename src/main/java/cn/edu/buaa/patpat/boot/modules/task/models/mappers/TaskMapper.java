package cn.edu.buaa.patpat.boot.modules.task.models.mappers;

import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskView;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    default List<TaskListView> query(int courseId, int type, boolean visibleOnly) {
        if (visibleOnly) {
            return queryVisibleImpl(courseId, type);
        } else {
            return queryAllImpl(courseId, type);
        }
    }

    @Select("""
            SELECT `id`, `title`, `visible`, `start_time`, `deadline_time`, `end_time`
            FROM `task`
            WHERE `course_id` = #{courseId} AND `type` = #{type} AND `visible` = 1
            ORDER BY `start_time`
            """)
    List<TaskListView> queryVisibleImpl(int courseId, int type);

    @Select("""
            SELECT `id`, `title`, `visible`, `start_time`, `deadline_time`, `end_time`
            FROM `task`
            WHERE `course_id` = #{courseId} AND `type` = #{type}
            ORDER BY `start_time`
            """)
    List<TaskListView> queryAllImpl(int courseId, int type);

    @Select("""
            SELECT * FROM `task`
            WHERE `id` = #{id} AND `course_id` = #{courseId} AND `type` = #{type}
            """)
    TaskView query(int id, int courseId, int type);
}
