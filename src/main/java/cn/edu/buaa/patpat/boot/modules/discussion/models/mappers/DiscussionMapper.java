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

    @Delete("DELETE FROM `discussion` WHERE `id` = #{id}")
    int deleteById(int id);
}
