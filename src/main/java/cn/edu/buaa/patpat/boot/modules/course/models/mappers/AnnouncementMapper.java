package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Announcement;
import cn.edu.buaa.patpat.boot.modules.course.models.views.AnnouncementBriefView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.AnnouncementView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace
public interface AnnouncementMapper {
    @Insert("""
            INSERT INTO `announcement` (
                            `course_id`,
                            `account_id`,
                            `title`,
                            `content`,
                            `topped`,
                            `created_at`,
                            `updated_at`)
            VALUES (
                #{courseId},
                #{accountId},
                #{title},
                #{content},
                #{topped},
                #{createdAt},
                #{updatedAt})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Announcement announcement);

    @Select("""
            SELECT `id`, `title`, `content`, `topped`
            FROM `announcement`
            WHERE `id` = #{id} AND `course_id` = #{courseId}
            """)
    Announcement findUpdate(int id, int courseId);

    @Update("""
            UPDATE `announcement`
            SET `title` = #{title},
                `content` = #{content},
                `topped` = #{topped},
                `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Announcement announcement);

    @Delete("DELETE FROM `announcement` WHERE `id` = #{id}")
    int delete(int id);

    @Select("""
            SELECT `ann`.`id`, `acc`.`name` AS `author`, `title`, `content`, `topped`, `created_at`, `updated_at`
            FROM (
                SELECT * FROM `announcement`
                WHERE `id` = #{id} AND `course_id` = #{courseId}
            ) AS `ann` JOIN (
                SELECT `id`, `name` FROM `account`
            ) AS `acc` ON `ann`.`account_id` = `acc`.`id`
            """)
    AnnouncementView find(int id, int courseId);

    @Select("""
            SELECT `ann`.`id`, `acc`.`name` AS `author`, `title`, `content`, `topped`, `created_at`, `updated_at`
            FROM (
                SELECT * FROM `announcement`
                WHERE `course_id` = #{courseId}
            ) AS `ann` JOIN (
                SELECT `id`, `name` FROM `account`
            ) AS `acc` ON `ann`.`account_id` = `acc`.`id`
            ORDER BY `topped` DESC, `created_at` DESC
            """)
    List<AnnouncementView> getAll(int courseId);

    @Select("""
            SELECT `id`, `title`, `content`
            FROM `announcement`
            WHERE `id` = #{id} AND `course_id` = #{courseId}
            """)
    AnnouncementBriefView findBrief(int id, int courseId);
}
