package cn.edu.buaa.patpat.boot.modules.task.models.mappers;

import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskView;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * We use readonly cache for tasks to improve the performance.
 * Therefore, the return results should not be modified unless @Options(useCache = false) is used.
 */
@Mapper
@CacheNamespace(readWrite = false)
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
    @Options(useCache = false)
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

    @Select("""
            SELECT `id`, `course_id`, `visible`, `start_time`, `deadline_time`, `end_time`
            FROM `task`
            WHERE `id` = #{id} AND `course_id` = #{courseId} AND `type` = #{type}
            """)
    Task findSubmit(int id, int courseId, int type);

    @Select("""
            SELECT COUNT(*)
            FROM `task`
            WHERE `id` = #{id} AND `course_id` = #{courseId} AND `type` = #{type}
            """)
    boolean existsByType(int id, int courseId, int type);

    @Select("""
            SELECT COUNT(*)
            FROM `task`
            WHERE `id` = #{id} AND `course_id` = #{courseId}
            """)
    boolean exists(int id, int courseId);

    @Select("""
            SELECT `title`
            FROM `task`
            WHERE `id` = #{id}
            """)
    Task findTitle(int id);

    default List<TaskListView> query(int courseId, int type, boolean visibleOnly) {
        if (visibleOnly) {
            return queryVisibleImpl(courseId, type);
        } else {
            return queryAllImpl(courseId, type);
        }
    }
}
