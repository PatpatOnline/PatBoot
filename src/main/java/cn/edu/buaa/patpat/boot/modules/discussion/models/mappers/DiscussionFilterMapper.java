package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DiscussionFilterMapper {
    @Select("""
            SELECT `id`, `type`, `author_id`, `course_id`, `title`, `content`,
                    `topped`, `starred`, `created_at`, `updated_at`,
                    (SELECT COUNT(*) FROM `like_discussion` WHERE `discussion_id` = `id`) AS `like_count`,
                    (SELECT EXISTS(SELECT 1 FROM `like_discussion` WHERE `account_id` = #{accountId} AND `discussion_id` = `id`)) AS `liked`,
                    (SELECT COUNT(*) FROM `reply` WHERE `discussion_id` = `id`) AS `reply_count`
            FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    DiscussionView findWithAccount(int courseId, int discussionId, int accountId);
}
