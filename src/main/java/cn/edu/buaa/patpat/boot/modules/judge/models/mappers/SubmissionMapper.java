/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import org.apache.ibatis.annotations.*;

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

    @Select("""
            SELECT `id`, `problem_id`, `account_id`, `submit_time`, `start_time`, `end_time`
            FROM `submission`
            WHERE `problem_id` = #{problemId} AND `account_id` = #{accountId}
            ORDER BY `id` DESC
            LIMIT 1
            """)
    Submission findCheckLast(int problemId, int accountId);

    @Delete("DELETE FROM `submission` WHERE `id` = #{id}")
    void delete(int id);
}
