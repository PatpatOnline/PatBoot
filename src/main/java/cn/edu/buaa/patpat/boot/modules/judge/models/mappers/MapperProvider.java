/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import cn.edu.buaa.patpat.boot.common.utils.Strings;

public class MapperProvider {
    /**
     * If filter.id is not null, then it should be handled outside of this method.
     * If accountIds is not null, then its length should be greater than 0.
     */
    public String count(int courseId, SubmissionFilter filter) {
        return String.format("""
                        SELECT COUNT(*)
                        FROM `submission`
                        WHERE `course_id` = #{courseId} AND %s AND %s AND %s AND %s AND %s
                        """,
                Strings.isNullOrBlank(filter.getBuaaId()) ? "TRUE" : "`buaa_id` LIKE CONCAT('%', #{filter.buaaId}, '%')",
                Strings.isNullOrBlank(filter.getAccountIds()) ? "TRUE" : "`account_id` IN (#{filter.accountIds})",
                filter.getProblemId() == null ? "TRUE" : "`problem_id` = #{filter.problemId}",
                filter.getMinScore() == null ? "TRUE" : "`score` >= #{filter.minScore}",
                filter.getMaxScore() == null ? "TRUE" : "`score` <= #{filter.maxScore}");
    }

    public String query(int courseId, int pageSize, int offset, SubmissionFilter filter) {
        return String.format("""
                        SELECT `s`.`id`, `s`.`account_id`, `s`.`language`, `s`.`score`,
                               `s`.`submit_time`, `s`.`start_time`, `s`.`end_time`,
                               `s`.`buaa_id`, `a`.`name`, `s`.`problem_id`, `p`.`problem_name`
                        FROM (
                            SELECT `id`, `account_id`, `buaa_id`, `problem_id`, `language`,
                            `score`, `submit_time`, `start_time`, `end_time`
                            FROM `submission`
                            WHERE `course_id` = #{courseId} AND %s AND %s AND %s AND %s AND %s
                            ORDER BY `id` DESC
                            LIMIT #{pageSize} OFFSET #{offset}
                        ) AS `s` JOIN (
                            SELECT `id`, `name` FROM `account`
                        ) AS `a` ON `s`.`account_id` = `a`.`id` JOIN (
                            SELECT `id`, `title` AS `problem_name` FROM `problem`
                        ) AS `p` ON `s`.`problem_id` = `p`.`id`
                        """,
                Strings.isNullOrBlank(filter.getBuaaId()) ? "TRUE" : "`buaa_id` LIKE CONCAT('%', #{filter.buaaId}, '%')",
                Strings.isNullOrBlank(filter.getAccountIds()) ? "TRUE" : "`account_id` IN (#{filter.accountIds})",
                filter.getProblemId() == null ? "TRUE" : "`problem_id` = #{filter.problemId}",
                filter.getMinScore() == null ? "TRUE" : "`score` >= #{filter.minScore}",
                filter.getMaxScore() == null ? "TRUE" : "`score` <= #{filter.maxScore}");
    }
}
