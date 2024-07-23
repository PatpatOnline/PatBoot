package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Reply;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.ReplyView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReplyMapper {
    @Insert("""
            INSERT INTO `reply` (
                `discussion_id`, `parent_id`, `author_id`, `content`, `created_at`, `updated_at`
            ) VALUES (
                #{discussionId}, #{parentId}, #{authorId}, #{content}, #{createdAt}, #{updatedAt}
            )
            """)
    void save(Reply reply);

    @Update("""
            UPDATE `reply`
            SET `content` = #{content}, `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    int update(Reply reply);

    @Update("UPDATE `reply` SET `verified` = #{verified} WHERE `id` = #{id}")
    int updateVerified(int id, boolean verified);

    /**
     * Cascade delete, delete the reply and its children.
     */
    @Delete("DELETE FROM `reply` WHERE `id` = #{id} OR `parent_id` = #{id}")
    int deleteById(int id);

    @Delete("DELETE FROM `reply` WHERE `discussion_id` = #{discussionId}")
    int deleteByDiscussionId(int discussionId);

    @Select("""
            SELECT `id`, `parent_id`, `author_id`, `content`, `verified`, `created_at`, `updated_at`, `l`.`like_count`,
                    (SELECT EXISTS(SELECT 1 FROM `like_reply` WHERE `account_id` = #{accountId} AND `reply_id` = `id`)) AS `liked`
            FROM (
                SELECT `id`, `parent_id`, `author_id`, `content`, `verified`, `created_at`, `updated_at`
                FROM `reply` WHERE `discussion_id` = #{discussionId}
                ORDER BY `created_at`
            ) AS `r` LEFT JOIN (
                SELECT `reply_id`, COUNT(*) AS `like_count`
                    FROM `like_reply`
                    WHERE `reply_id` IN (SELECT `id` FROM `reply` WHERE `discussion_id` = #{discussionId})
                    GROUP BY `reply_id`) AS `l` ON `r`.`id` = `l`.`reply_id`
            """)
    List<ReplyView> getAllByDiscussionId(int discussionId, int accountId);
}
