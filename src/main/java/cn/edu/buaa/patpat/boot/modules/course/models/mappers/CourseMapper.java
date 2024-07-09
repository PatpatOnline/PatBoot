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
    int insert(Course course);

    @Select("SELECT * FROM `course` WHERE `id` = #{id}")
    Course findById(int id);

    @Delete("DELETE FROM `course` WHERE `id` = #{id}")
    int deleteById(int id);

    @Update("""
            UPDATE `course`
            SET `name` = #{name},
                `code` = #{code},
                `semester` = #{semester},
                `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    int update(Course course);

    @Select("SELECT * FROM `course` ORDER BY `semester` DESC")
    List<Course> getAll();
}
