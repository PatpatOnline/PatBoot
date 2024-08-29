/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountDetailView;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountListView;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface AccountFilterMapper {
    @Select("SELECT COUNT(*) FROM `account` WHERE `buaa_id` = #{buaaId}")
    boolean exists(String buaaId);

    @Select("SELECT * FROM `account` WHERE `id` = #{id} LIMIT 1")
    Account findById(int id);

    @Select("SELECT * FROM `account` WHERE `buaa_id` = #{buaaId} LIMIT 1")
    Account findByBuaaId(String buaaId);

    @Select("SELECT * FROM `account` WHERE `name` = #{name} LIMIT 1")
    Account findByName(String name);

    @Select("SELECT `id`, `teacher`, `ta` FROM `account` WHERE `id` = #{id} LIMIT 1")
    Account findRole(int id);

    @Select("""
            SELECT `id`, `buaa_id`, `name`, `gender`, `school`, `teacher`, `ta`, `avatar`
            FROM `account`
            WHERE `id` = #{id}
            """)
    AccountDetailView findDetailView(int id);

    @SelectProvider(type = MapperProvider.class, method = "count")
    int count(AccountFilter filter);

    @SelectProvider(type = MapperProvider.class, method = "query")
    List<AccountListView> queryImpl(int pageSize, int offset, AccountFilter filter);

    @Select("SELECT `id`, `name` FROM `account` WHERE `teacher` = 1 AND `id` != 1")
    List<TeacherIndexView> queryTeachers();

    @Select("SELECT `id`, `name` FROM `account` WHERE `teacher` = 1 AND `id` = #{id}")
    TeacherIndexView queryTeacher(int id);

    default List<AccountListView> query(int page, int pageSize, AccountFilter filter) {
        return queryImpl(pageSize, (page - 1) * pageSize, filter);
    }
}
