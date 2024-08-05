package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.views.ReplyView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReplyFilterMapper {
    @Select("""
            SELECT `id`, `parent_id`, `to_id`, `author_id`, `content`, `verified`, `created_at`, `updated_at`, `l`.`like_count`,
                    (SELECT EXISTS(SELECT 1 FROM `like_reply` WHERE `account_id` = #{accountId} AND `reply_id` = `id`)) AS `liked`
            FROM (
                SELECT `id`, `parent_id`, `to_id`, `author_id`, `content`, `verified`, `created_at`, `updated_at`
                FROM `reply` WHERE `discussion_id` = #{discussionId}
                ORDER BY `created_at`
            ) AS `r` LEFT JOIN (
                SELECT `reply_id`, COUNT(*) AS `like_count`
                    FROM `like_reply`
                    WHERE `reply_id` IN (SELECT `id` FROM `reply` WHERE `discussion_id` = #{discussionId})
                    GROUP BY `reply_id`) AS `l` ON `r`.`id` = `l`.`reply_id`
            """)
    List<ReplyView> query(int discussionId, int accountId);

    @Select("""
            SELECT `id`, `author_id`, `parent_id`, `to_id`, `content`, `verified`, `created_at`, `updated_at`,
                    (SELECT COUNT(*) FROM `like_reply` WHERE `reply_id` = `id`) AS `like_count`,
                    (SELECT EXISTS(SELECT 1 FROM `like_reply` WHERE `account_id` = #{accountId} AND `reply_id` = `id`)) AS `liked`
            FROM `reply`
            WHERE `id` = #{replyId}
            """)
    ReplyView find(int replyId, int accountId);

    @Select("""
            SELECT COUNT(*) FROM `reply`
            WHERE `id` = #{replyId} AND `discussion_id` = #{discussionId}
            """)
    boolean exists(int discussionId, int replyId);
}
