package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionAccountView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface DiscussionAccountMapper {
    @Select("""
            SELECT `id`, `buaa_id`, `name`, `avatar`, `ta`, `teacher`
            FROM `account`
            WHERE `id` = #{id}
            """)
    DiscussionAccountView findById(int id);

    @SelectProvider(type = MapperProvider.class, method = "findByIds")
    List<DiscussionAccountView> findByIds(Iterable<Integer> ids);
}
