package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Discussion;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DiscussionMapper {
    @Insert("""
            INSERT INTO `discussion` (
                `type`, `course_id`, `author_id`, `title`, `content`, `created_at`, `updated_at`
            ) VALUES (
                #{type}, #{courseId}, #{authorId}, #{title}, #{content}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Discussion discussion);

    @Select("""
            SELECT `id`, `type`, `title`, `content` FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    Discussion findUpdate(int courseId, int discussionId);

    @Update("""
            UPDATE `discussion`
            SET `type` = #{type}, `title` = #{title}, `content` = #{content}, `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Discussion discussion);

    @Update("UPDATE `discussion` SET `topped` = #{topped} WHERE `id` = #{id}")
    int updateTopped(int id, boolean topped);

    @Update("UPDATE `discussion` SET `starred` = #{starred} WHERE `id` = #{id}")
    int updateStarred(int id, boolean starred);

    @Select("""
            SELECT `id`, `course_id`, `author_id`, `title` FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    Discussion findDelete(int courseId, int discussionId);

    @Delete("DELETE FROM `discussion` WHERE `id` = #{id}")
    void delete(int id);

    @Insert("""
            INSERT IGNORE INTO `like_discussion` (`account_id`, `discussion_id`)
            VALUES (#{accountId}, #{id})
            """)
    void like(int id, int accountId);

    @Delete("""
            DELETE FROM `like_discussion`
            WHERE `account_id` = #{accountId} AND `discussion_id` = #{id}
            """)
    void unlike(int id, int accountId);
}
