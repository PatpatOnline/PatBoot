/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.models.mappers;

import cn.edu.buaa.patpat.boot.modules.problem.models.views.ProblemListView;
import cn.edu.buaa.patpat.boot.modules.problem.models.views.ProblemSelectView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ProblemFilterMapper {
    @Select("SELECT `id`, `title` FROM `problem` ORDER BY `created_at` DESC")
    List<ProblemSelectView> getAll();

    @SelectProvider(type = MapperProvider.class, method = "count")
    int count(ProblemFilter filter);

    @SelectProvider(type = MapperProvider.class, method = "query")
    List<ProblemListView> queryImpl(int pageSize, int offset, ProblemFilter filter);

    default List<ProblemListView> query(int page, int pageSize, ProblemFilter filter) {
        return queryImpl(pageSize, (page - 1) * pageSize, filter);
    }
}
