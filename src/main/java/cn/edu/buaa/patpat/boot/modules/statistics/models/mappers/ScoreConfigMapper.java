/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.models.mappers;

import cn.edu.buaa.patpat.boot.modules.statistics.models.entities.ScoreConfig;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@CacheNamespace
public interface ScoreConfigMapper {
    @Insert("""
            INSERT INTO `score_config` (
                `course_id`, `lab_score`, `iter_score`, `proj_score`, `late_percent`
            ) VALUES (
                #{courseId}, #{labScore}, #{iterScore}, #{projScore}, #{latePercent}
            ) ON DUPLICATE KEY UPDATE
                `lab_score` = #{labScore},
                `iter_score` = #{iterScore},
                `proj_score` = #{projScore},
                `late_percent` = #{latePercent}
            """)
    void saveOrUpdate(ScoreConfig config);

    @Select("SELECT * FROM `score_config` WHERE `course_id` = #{courseId}")
    ScoreConfig find(int courseId);
}
