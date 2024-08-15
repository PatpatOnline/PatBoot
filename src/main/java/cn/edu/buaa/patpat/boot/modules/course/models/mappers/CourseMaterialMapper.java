package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.CourseMaterial;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMaterialMapper {
    @Insert("""
            INSERT INTO `course_material` (
                `course_id`, `filename`, `comment`, `created_at`, `updated_at`
            ) VALUES (
                #{courseId}, #{filename}, #{comment}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(CourseMaterial material);

    @Update("""
            UPDATE `course_material`
            SET `filename` = #{filename}, `comment` = #{comment}, `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(CourseMaterial material);

    @Delete("DELETE FROM `course_material` WHERE `id` = #{id} AND `course_id` = #{courseId}")
    int delete(int id, int courseId);

    @Select("""
            SELECT COUNT(*) FROM `course_material`
            WHERE `course_id` = #{courseId} AND `filename` = #{filename}
            """)
    boolean existsByCourseIdAndFilename(int courseId, String filename);

    @Select("SELECT * FROM `course_material` WHERE `id` = #{id} AND `course_id` = #{courseId}")
    CourseMaterial find(int id, int courseId);

    @Select("SELECT * FROM `course_material` WHERE `course_id` = #{courseId}")
    List<CourseMaterial> query(int courseId);
}
