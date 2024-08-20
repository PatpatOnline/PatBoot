package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentExportView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentImportView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentImportMapper {
    @Select("""
             SELECT
                 `stu`.`id`,
                 `stu`.`account_id`,
                 `stu`.`course_id`,
                 `stu`.`teacher_id`,
                 `acc`.`buaa_id`
             FROM (
                SELECT `id`, `account_id`, `course_id`, `teacher_id` FROM `student`
                WHERE `course_id` = #{courseId} AND `teacher_id` = #{teacherId}
            ) AS `stu` JOIN (
                 SELECT `id`, `buaa_id` FROM `account`
            ) AS `acc` ON `stu`.`account_id` = `acc`.`id`
            """)
    List<StudentImportView> getAllImport(int courseId, int teacherId);

    @Select("""
            SELECT DISTINCT (`course_id`) FROM `student`
            WHERE `account_id` = #{accountId}
            """)
    List<Integer> getAllCourseId(int accountId);

    @Select("""
            SELECT `buaa_id`, `name`, `gender`, `school`, `major`, `class_name`, `repeat`
            FROM (
                SELECT `account_id`, `major`, `class_name`, `repeat` FROM `student`
                WHERE `course_id` = #{courseId} AND `teacher_id` = #{teacherId}
            ) AS `stu` JOIN (
                SELECT `id`, `buaa_id`, `name`, `gender`, `school` FROM `account`
            ) AS `acc` ON `stu`.`account_id` = `acc`.`id`
            """)
    List<StudentExportView> getAllExport(int courseId, int teacherId);
}
