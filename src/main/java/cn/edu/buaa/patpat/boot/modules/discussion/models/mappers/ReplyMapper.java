package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Reply;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ReplyMapper {
    @Insert("""
            INSERT INTO `reply` (
                `discussion_id`, `parent_id`, `to_id`, `author_id`,
                `content`, `created_at`, `updated_at`
            ) VALUES (
                #{discussionId}, #{parentId}, #{toId}, #{authorId},
                #{content}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Reply reply);

    @Update("""
            UPDATE `reply`
            SET `content` = #{content}, `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Reply reply);

    @Update("UPDATE `reply` SET `verified` = #{verified} WHERE `id` = #{id}")
    int updateVerified(int id, boolean verified);

    /**
     * Cascade delete, delete the reply and its children.
     */
    @Delete("DELETE FROM `reply` WHERE `id` = #{id} OR `parent_id` = #{id}")
    void deleteById(int id);

    @Delete("DELETE FROM `reply` WHERE `discussion_id` = #{discussionId}")
    int deleteByDiscussionId(int discussionId);

    @Insert("""
            INSERT IGNORE INTO `like_reply` (`account_id`, `reply_id`)
            VALUES (#{accountId}, #{id})
            """)
    void like(int id, int accountId);

    @Delete("""
            DELETE FROM `like_reply`
            WHERE `account_id` = #{accountId} AND `reply_id` = #{id}
            """)
    void unlike(int id, int accountId);

    @Select("""
            SELECT `id`, `discussion_id`, `author_id`
            FROM `reply`
            WHERE `id` = #{replyId}
            """)
    @Options(useCache = true)
    Reply findUpdate(int replyId);

    @Select("""
            SELECT `id`, `discussion_id`, `author_id`
            FROM `reply`
            WHERE `id` = #{replyId}
            """)
    @Options(useCache = true)
    Reply findDelete(int replyId);

    @Select("SELECT `id`, `discussion_id` FROM `reply` WHERE `id` = #{replyId}")
    @Options(useCache = true)
    Reply findLike(int replyId);

    @Select("""
            SELECT `id`, `parent_id`, `to_id`
            FROM `reply`
            WHERE `id` = #{replyId} AND `discussion_id` = #{discussionId}
            """)
    Reply findTo(int discussionId, int replyId);
}
