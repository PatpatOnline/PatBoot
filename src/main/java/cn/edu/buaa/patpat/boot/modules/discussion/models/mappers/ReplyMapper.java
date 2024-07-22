package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Reply;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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
}
