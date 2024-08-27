package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import cn.edu.buaa.patpat.boot.common.utils.Strings;

class MapperProvider {
    /**
     * If filter.id is not null, it should be handled earlier.
     */
    public String count(AccountFilter filter) {
        return String.format("""
                        SELECT COUNT(*)
                        FROM `account`
                        WHERE %s AND %s AND %s
                        """,
                Strings.isNullOrBlank(filter.getBuaaId()) ? "TRUE" : "`buaa_id` LIKE CONCAT('%', #{buaaId}, '%')",
                Strings.isNullOrBlank(filter.getName()) ? "TRUE" : "`name` LIKE CONCAT('%', #{name}, '%')",
                filter.getRole() == null ? "TRUE" : switch (filter.getRole()) {
                    case 0 -> "NOT `ta`";
                    case 1 -> "`ta` AND NOT `teacher`";
                    case 2 -> "`teacher`";
                    default -> "FALSE";
                });
    }

    public String query(int pageSize, int offset, AccountFilter filter) {
        return String.format("""
                        SELECT `id`, `buaa_id`, `name`, `gender`, `school`, `teacher`, `ta`, `created_at`
                        FROM `account`
                        WHERE %s AND %s AND %s
                        LIMIT #{pageSize} OFFSET #{offset}
                        """,
                Strings.isNullOrBlank(filter.getBuaaId()) ? "TRUE" : "`buaa_id` LIKE CONCAT('%', #{filter.buaaId}, '%')",
                Strings.isNullOrBlank(filter.getName()) ? "TRUE" : "`name` LIKE CONCAT('%', #{filter.name}, '%')",
                filter.getRole() == null ? "TRUE" : switch (filter.getRole()) {
                    case 0 -> "NOT `ta`";
                    case 1 -> "`ta` AND NOT `teacher`";
                    case 2 -> "`teacher`";
                    default -> "FALSE";
                });
    }
}
