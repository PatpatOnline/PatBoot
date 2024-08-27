package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.common.utils.Strings;

class MapperProvider {
    public String count(int courseId, StudentFilter filter) {
        return String.format("""
                        SELECT COUNT(*)
                        FROM (
                            SELECT `id`, `account_id`, `teacher_id` FROM `student`
                            WHERE `course_id` = #{courseId}
                        ) AS `stu` JOIN (
                            SELECT `id`, `buaa_id`, `name` FROM `account`
                        ) AS `as` ON `stu`.account_id = `as`.`id`
                        WHERE %s AND %s AND %s
                        """,
                Strings.isNullOrBlank(filter.getBuaaId()) ? "TRUE" : "`as`.`buaa_id` LIKE CONCAT('%', #{filter.buaaId}, '%')",
                Strings.isNullOrBlank(filter.getName()) ? "TRUE" : "`as`.`name` LIKE CONCAT('%', #{filter.name}, '%')",
                filter.getTeacherId() == null ? "TRUE" : "`stu`.`teacher_id` = #{filter.teacherId}");
    }

    public String query(int courseId, int pageSize, int offset, StudentFilter filter) {
        return String.format("""
                        SELECT
                            `stu`.`id`,
                            `stu`.`account_id`,
                            `as`.`buaa_id`,
                            `as`.`name` AS `student_name`,
                            `stu`.`teacher_id`,
                            `as`.`school`,
                            `stu`.`major`,
                            `stu`.`class_name`,
                            `stu`.`repeat`,
                            `stu`.`created_at`
                        FROM (
                            SELECT * FROM `student`
                            WHERE `course_id` = #{courseId}
                        ) AS `stu` JOIN (
                            SELECT `id`, `buaa_id`, `name`, `school` FROM `account`
                        ) AS `as` ON `stu`.account_id = `as`.`id`
                        WHERE %s AND %s AND %s
                        ORDER BY `as`.`buaa_id` ASC
                        LIMIT #{pageSize} OFFSET #{offset}
                        """,
                Strings.isNullOrBlank(filter.getBuaaId()) ? "TRUE" : "`as`.`buaa_id` LIKE CONCAT('%', #{filter.buaaId}, '%')",
                Strings.isNullOrBlank(filter.getName()) ? "TRUE" : "`as`.`name` LIKE CONCAT('%', #{filter.name}, '%')",
                filter.getTeacherId() == null ? "TRUE" : "`stu`.`teacher_id` = #{filter.teacherId}");
    }
}
