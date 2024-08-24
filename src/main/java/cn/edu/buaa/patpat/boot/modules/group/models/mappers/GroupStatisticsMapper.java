package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupInfoView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreInfoView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupStatisticsMapper {
    @Select("SELECT `id`, `name` FROM `group` WHERE `course_id` = #{courseId}")
    List<GroupInfoView> getGroups(int courseId);

    @Select("SELECT `group_id`, `course_id`, `score` FROM `group_score` WHERE `course_id` = #{courseId}")
    List<GroupScoreInfoView> getGroupScores(int courseId);
}
