package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Discussion;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
