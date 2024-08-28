package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import org.apache.ibatis.annotations.Param;

public class MapperProvider {
    public String getBadgesByIds(@Param("ids") Iterable<Integer> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `id`, `buaa_id`, `name`, `avatar`, `ta`, `teacher`");
        sql.append(" FROM `account`");
        sql.append(" WHERE `id` IN (");
        for (Integer id : ids) {
            sql.append(id).append(",");
        }
        sql.setCharAt(sql.length() - 1, ')');
        return sql.toString();
    }

    public String count(int courseId, DiscussionFilter filter) {
        return String.format("""
                        SELECT COUNT(*)
                        FROM `discussion`
                        WHERE `course_id` = #{courseId} AND %s AND %s
                        """,
                Strings.isNullOrBlank(filter.getQuery()) ? "TRUE" : "((`title` LIKE CONCAT('%', #{filter.query}, '%')) OR (`content` LIKE CONCAT('%', #{filter.query}, '%')))",
                filter.getType() == null ? "TRUE" : "`type` = #{filter.type}"
        );
    }

    public String query(int courseId, int pageSize, int offset, DiscussionFilter filter) {
        return String.format("""
                        SELECT `id`, `type`, `author_id`, `course_id`, `title`, SUBSTRING(`content`, 1, 50) AS `content`,
                               `topped`, `starred`, `created_at`, `updated_at`, `l`.`like_count`, `c`.`reply_count`,
                               EXISTS(SELECT 1 FROM `like_discussion` WHERE `account_id` = #{accountId} AND `discussion_id` = `d`.`id`) AS `liked`,
                               EXISTS(SELECT 1 FROM `subscription` WHERE `account_id` = #{accountId} AND `discussion_id` = `d`.`id`) AS `subscribed`
                        FROM (
                            SELECT `id`, `type`, `author_id`, `course_id`, `title`, `topped`, `starred`,
                                `created_at`, `updated_at`, `content`
                            FROM `discussion` WHERE `course_id` = #{courseId} AND %s AND %s
                            ORDER BY `topped` DESC, `created_at` DESC
                            LIMIT #{pageSize} OFFSET #{offset}
                        ) AS `d` LEFT JOIN (
                            SELECT `discussion_id`, COUNT(*) AS `like_count`
                            FROM `like_discussion` GROUP BY `discussion_id`
                        ) AS `l` ON `d`.`id` = `l`.`discussion_id` LEFT JOIN (
                            SELECT `discussion_id`, COUNT(*) AS `reply_count`
                            FROM `reply` GROUP BY `discussion_id`) AS `c` ON `d`.`id` = `c`.`discussion_id`
                        """,
                Strings.isNullOrBlank(filter.getQuery()) ? "TRUE" : "((`title` LIKE CONCAT('%', #{filter.query}, '%')) OR (`content` LIKE CONCAT('%', #{filter.query}, '%')))",
                filter.getType() == null ? "TRUE" : "`type` = #{filter.type}"
        );
    }
}
