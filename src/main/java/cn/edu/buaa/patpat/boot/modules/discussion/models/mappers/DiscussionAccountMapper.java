/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

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
    DiscussionAccountView find(int id);

    @SelectProvider(type = MapperProvider.class, method = "getBadgesByIds")
    List<DiscussionAccountView> getAllImpl(Iterable<Integer> ids);

    default List<DiscussionAccountView> getAll(Iterable<Integer> ids) {
        if (ids.iterator().hasNext()) {
            return getAllImpl(ids);
        } else {
            return List.of();
        }
    }
}
