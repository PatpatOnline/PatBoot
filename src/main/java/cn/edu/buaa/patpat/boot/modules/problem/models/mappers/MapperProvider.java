/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.models.mappers;

import cn.edu.buaa.patpat.boot.common.utils.Strings;

public class MapperProvider {
    public String count(ProblemFilter filter) {
        if (filter.isEmpty()) {
            return "SELECT COUNT(*) FROM `problem`";
        }
        return String.format("""
                        SELECT COUNT(*)
                        FROM `problem`
                        WHERE %s AND %s
                        """,
                Strings.isNullOrBlank(filter.getTitle()) ? "TRUE" : "`title` LIKE CONCAT('%', #{title}, '%')",
                filter.getHidden() == null ? "TRUE" : "`hidden` = #{hidden}");
    }

    public String query(int pageSize, int offset, ProblemFilter filter) {
        if (filter.isEmpty()) {
            return String.format("""
                            SELECT `id`, `title`, `hidden`, `created_at`, `updated_at`
                            FROM `problem`
                            ORDER BY `created_at` DESC
                            LIMIT %d OFFSET %d
                            """,
                    pageSize, offset);
        }
        return String.format("""
                        SELECT `id`, `title`, `hidden`, `created_at`, `updated_at`
                        FROM `problem`
                        WHERE %s AND %s
                        ORDER BY `created_at` DESC
                        LIMIT %d OFFSET %d
                        """,
                Strings.isNullOrBlank(filter.getTitle()) ? "TRUE" : "`title` LIKE CONCAT('%', #{filter.title}, '%')",
                filter.getHidden() == null ? "TRUE" : "`hidden` = #{filter.hidden}",
                pageSize, offset);
    }
}
