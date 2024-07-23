package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import org.apache.ibatis.annotations.Param;

public class MapperProvider {
    public String getAllByIds(@Param("ids") Iterable<Integer> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `id`, `buaa_id`, `name`, `avatar`, `ta`, `teacher`");
        sql.append(" FROM `account`");
        sql.append(" WHERE `id` IN (");
        for (Integer id : ids) {
            sql.append(id).append(",");
        }
        sql.setCharAt(sql.length() - 1, ')');
        return sql.toString();
    }
}
