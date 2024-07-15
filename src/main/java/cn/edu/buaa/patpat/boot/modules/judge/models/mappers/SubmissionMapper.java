package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SubmissionMapper {
    @Insert("""
            INSERT INTO `submission` (
                `account_id`, `buaa_id`, `course_id`, `problem_id`, `language`, `submit_time`
            ) VALUES (
                #{accountId}, #{buaaId}, #{courseId}, #{problemId}, #{language}, #{submitTime}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void initialize(Submission submission);

    @Update("""
            UPDATE `submission`
            SET `start_time` = #{startTime},
                `end_time` = #{endTime},
                `score` = #{score},
                `data` = #{data}
            WHERE `id` = #{id}
            """)
    int finalize(Submission submission);
}
