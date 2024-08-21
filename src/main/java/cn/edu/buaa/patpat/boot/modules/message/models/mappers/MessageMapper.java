package cn.edu.buaa.patpat.boot.modules.message.models.mappers;

import cn.edu.buaa.patpat.boot.modules.message.models.entities.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("""
            INSERT INTO `message` (
                `type`, `course_id`, `account_id`, `content`, `argument`, `read`, `created_at`
            ) VALUES (
                #{type}, #{courseId}, #{accountId}, #{content}, #{argument}, #{read}, #{createdAt}
            )
            """)
    void save(Message message);

    @Insert("""
            <script>
            INSERT INTO `message` (
                `type`, `course_id`, `account_id`, `content`, `argument`, `read`, `created_at`
            ) VALUES
            <foreach collection="messages" item="message" separator=",">
                (
                    #{message.type}, #{message.courseId}, #{message.accountId},
                    #{message.content}, #{message.argument}, #{message.read}, #{message.createdAt}
                )
            </foreach>
            </script>
            """)
    void batchSave(List<Message> messages);

    @Update("UPDATE `message` SET `argument` = #{argument} WHERE `id` = #{id} AND `account_id` = #{accountId}")
    int update(int id, int accountId, String argument);

    @Update("UPDATE `message` SET `read` = #{read} WHERE `id` = #{id} AND `account_id` = #{accountId}")
    int read(int id, int accountId, boolean read);

    @Update("DELETE FROM `message` WHERE `id` = #{id} AND `account_id` = #{accountId}")
    int delete(int id, int accountId);

    @Select("""
            SELECT * FROM `message`
            WHERE `course_id` = #{courseId} AND `account_id` = #{accountId}
            ORDER BY `created_at` DESC
            """)
    List<Message> query(int courseId, int accountId);
}
