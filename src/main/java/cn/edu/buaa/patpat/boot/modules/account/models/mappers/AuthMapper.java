package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import cn.edu.buaa.patpat.boot.modules.account.models.views.AuthView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {
    @Select("""
            SELECT `id`, `buaa_id`, `name`, `password`, `ta`, `teacher`
            FROM `account`
            WHERE `buaa_id` = #{buaaId}
            """)
    AuthView find(String buaaId);
}
