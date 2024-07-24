package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Discussion;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiscussionFilterMapper {
    @Select("""
            SELECT `id`, `type`, `author_id`, `course_id`, `title`, `content`,
                    `topped`, `starred`, `created_at`, `updated_at`,
                    (SELECT COUNT(*) FROM `like_discussion` WHERE `discussion_id` = #{discussionId}) AS `like_count`,
                    (SELECT EXISTS(SELECT 1 FROM `like_discussion` WHERE `account_id` = #{accountId} AND `discussion_id` = #{discussionId})) AS `liked`,
                    (SELECT COUNT(*) FROM `reply` WHERE `discussion_id` = #{discussionId}) AS `reply_count`
            FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    DiscussionView find(int courseId, int discussionId, int accountId);

    @Select("""
            SELECT `id`, `type`, `title`, `content` FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    Discussion findUpdate(int courseId, int discussionId);

    @Select("""
            SELECT `id`, `course_id`, `author_id`, `title` FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    Discussion findDelete(int courseId, int discussionId);

    @Select("SELECT COUNT(*) FROM `discussion` WHERE `id` = #{discussionId} AND `course_id` = #{courseId}")
    boolean exists(int courseId, int discussionId);

    /**
     * Get all discussions in a course.
     *
     * @param courseId  Course id
     * @param accountId Account id
     * @param page      1-based page number
     * @param pageSize  Page size
     * @return List of discussions
     */
    default List<DiscussionView> getAll(int courseId, int accountId, int page, int pageSize) {
        return getAllImpl(courseId, accountId, pageSize, (page - 1) * pageSize);
    }

    @Select("""
            SELECT `id`, `type`, `author_id`, `course_id`, `title`, `content`,
                    `topped`, `starred`, `created_at`, `updated_at`,
                    `l`.`like_count`, `c`.`reply_count`,
                    EXISTS(SELECT 1 FROM `like_discussion` WHERE `account_id` = #{accountId} AND `discussion_id` = `d`.`id`) AS `liked`
            FROM (
                SELECT `id`, `type`, `author_id`, `course_id`, `title`, `topped`, `starred`,
                    `created_at`, `updated_at`, SUBSTRING(`content`, 1, 50) AS `content`
                FROM `discussion` WHERE `course_id` = #{courseId}
                ORDER BY `topped` DESC, `created_at` DESC
                LIMIT #{pageSize} OFFSET #{offset}
            ) AS `d` LEFT JOIN (
                SELECT `discussion_id`, COUNT(*) AS `like_count`
                FROM `like_discussion` GROUP BY `discussion_id`
            ) AS `l` ON `d`.`id` = `l`.`discussion_id` LEFT JOIN (
                SELECT `discussion_id`, COUNT(*) AS `reply_count`
                FROM `reply` GROUP BY `discussion_id`) AS `c` ON `d`.`id` = `c`.`discussion_id`
            """)
    List<DiscussionView> getAllImpl(int courseId, int accountId, int pageSize, int offset);

    @Select("SELECT COUNT(*) FROM `discussion` WHERE `course_id` = #{courseId}")
    int countAll(int courseId);
}
