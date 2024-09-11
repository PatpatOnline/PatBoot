/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupScore;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreInfoView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreListView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespaceRef(GroupMapper.class)
public interface GroupScoreMapper {
    /**
     * This is only called when students upload their group assignments, so {@code score}
     * is not updated.
     */
    @Insert("""
            INSERT INTO `group_score` (
                `group_id`, `course_id`, `score`, `record`, `created_at`, `updated_at`
            ) VALUES (
                #{groupId}, #{courseId}, #{score}, #{record}, #{createdAt}, #{updatedAt}
            ) ON DUPLICATE KEY UPDATE
                `record` = VALUES(`record`),
                `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrUpdate(GroupScore score);

    @Update("UPDATE `group_score` SET `score` = #{score} WHERE `group_id` = #{groupId}")
    void updateScore(int groupId, int score);

    @Select("SELECT * FROM `group_score` WHERE `group_id` = #{groupId}")
    GroupScore find(int groupId);

    @Select("SELECT * FROM `group_score` WHERE `course_id` = #{courseId}")
    List<GroupScoreListView> findInCourse(int courseId);

    @Select("SELECT * FROM `group_score` WHERE `group_id` = #{groupId}")
    GroupScoreListView findById(int groupId);

    @Select("SELECT `group_id`, `course_id`, `score` FROM `group_score` WHERE `course_id` = #{courseId}")
    List<GroupScoreInfoView> getGroupScores(int courseId);
}
