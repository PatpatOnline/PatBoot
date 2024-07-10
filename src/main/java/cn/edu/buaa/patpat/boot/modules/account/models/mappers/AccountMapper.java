package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Insert("""
            INSERT INTO `account` (
                       `buaa_id`,
                       `name`,
                       `password`,
                       `gender`,
                       `school`,
                       `ta`,
                       `teacher`,
                       `avatar`)
            VALUES (
                #{buaaId},
                #{name},
                #{password},
                #{gender},
                #{school},
                #{ta},
                #{teacher},
                #{avatar})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Account account);

    @Select("SELECT COUNT(*) FROM `account` WHERE `buaa_id` = #{buaaId}")
    boolean existsByBuaaId(String buaaId);

    @Select("SELECT * FROM `account` WHERE `id` = #{id} LIMIT 1")
    Account findById(int id);

    @Select("SELECT * FROM `account` WHERE `buaa_id` = #{buaaId} LIMIT 1")
    Account findByBuaaId(String buaaId);

    @Select("SELECT * FROM `account` WHERE `name` = #{name} LIMIT 1")
    Account findByName(String name);

    @Update("""
            UPDATE `account`
            SET `buaa_id` = #{buaaId},
                `name` = #{name},
                `gender` = #{gender},
                `school` = #{school}
            WHERE `id` = #{id}
            """)
    int update(Account account);

    @Delete("DELETE FROM `account` WHERE `id` = #{id}")
    int deleteById(int id);
}
