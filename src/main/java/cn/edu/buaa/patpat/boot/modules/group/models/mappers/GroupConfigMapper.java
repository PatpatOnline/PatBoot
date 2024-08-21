package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GroupConfigMapper {
    @Insert("""
            INSERT INTO `group_config` (`course_id`, `max_size`, `min_weight`, `max_weight`, `enabled`)
            VALUES (#{courseId}, #{maxSize}, #{minWeight}, #{maxWeight}, #{enabled})
            """)
    void save(GroupConfig config);

    @Insert("""
            UPDATE `group_config`
            SET `max_size` = #{maxSize},
                `min_weight` = #{minWeight},
                `max_weight` = #{maxWeight},
                `enabled` = #{enabled}
            WHERE `course_id` = #{courseId}
            """)
    void update(GroupConfig config);

    @Select("SELECT * FROM `group_config` WHERE `course_id` = #{courseId}")
    @Options(useCache = true)
    GroupConfig find(int courseId);
}
