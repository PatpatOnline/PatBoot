/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.models.mappers;

import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProblemMapper {
    @Insert("""
            INSERT INTO `problem` (
               `title`, `description`, `hidden`, `data`, `created_at`, `updated_at`
            ) VALUES (
                #{title}, #{description}, #{hidden}, #{data}, #{createdAt}, #{updatedAt}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Problem problem);

    @Insert("""
            UPDATE `problem`
            SET `title` = #{title}, `description` = #{description}, `hidden` = #{hidden}, `updated_at` = #{updatedAt}
            WHERE `id` = #{id}
            """)
    void update(Problem problem);

    @Select("SELECT * FROM `problem` WHERE `id` = #{id}")
    Problem find(int id);

    @Insert("DELETE FROM `problem` WHERE `id` = #{id}")
    int delete(int id);


    @Select("SELECT `id`, `hidden` FROM `problem` WHERE `id` = #{id}")
    Problem findJudge(int id);
}
