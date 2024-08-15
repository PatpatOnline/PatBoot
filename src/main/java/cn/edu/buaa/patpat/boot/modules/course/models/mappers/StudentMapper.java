package cn.edu.buaa.patpat.boot.modules.course.models.mappers;

import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentMapper {
    @Insert("""
            INSERT INTO `student` (
                `account_id`, `course_id`, `teacher_id`, `major`, `class_name`, `repeat`, `created_at`
            ) VALUES (
                #{accountId}, #{courseId}, #{teacherId}, #{major}, #{className}, #{repeat}, #{createdAt}
            )
            """)
    void save(Student student);

    @Delete("DELETE FROM `student` WHERE `id` = #{id}")
    void delete(int id);

    @Update("""
            UPDATE `student`
            SET
                `account_id` = #{accountId},
                `course_id` = #{courseId},
                `teacher_id` = #{teacherId},
                `major` = #{major},
                `class_name` = #{className},
                `repeat` = #{repeat}
            WHERE `id` = #{id}
            """)
    void importUpdate(Student student);

    @Update("""
            UPDATE `student`
            SET
                `teacher_id` = #{teacherId},
                `major` = #{major},
                `class_name` = #{className},
                `repeat` = #{repeat}
            WHERE `id` = #{id}
            """)
    void update(Student student);
}
