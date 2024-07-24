package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Insert("""
            INSERT INTO `course` (
                `name`, `code`, `semester`, `active`, `created_at`, `updated_at`
            )
            VALUES (
                #{name}, #{code}, #{semester}, #{active}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Course course);

    @Select("SELECT * FROM `course` WHERE `id` = #{id}")
    Course find(int id);

    @Select("""
            SELECT * FROM `course`
            WHERE `id` = (
                SELECT `course_id` FROM `student`
                WHERE `course_id` = #{id} AND `account_id` = #{accountId}
            ) AND `active` = true
            """)
    Course findActive(int id, int accountId);

    @Select("""
            SELECT * FROM `course`
            WHERE `active` = true AND `id` IN (
                SELECT `course_id` FROM `student`
                WHERE `account_id` = #{accountId}
            )
            """)
    List<Course> findAllActive(int accountId);

    @Delete("DELETE FROM `course` WHERE `id` = #{id}")
    void delete(int id);

    @Update("""
            UPDATE `course`
            SET `name` = #{name},
                `code` = #{code},
                `semester` = #{semester},
                `active` = #{active},
                `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Course course);

    @Select("SELECT * FROM `course` ORDER BY `semester` DESC")
    List<Course> getAll();
}
