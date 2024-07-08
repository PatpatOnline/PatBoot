package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {
    @Insert("""
            INSERT INTO `account` (
                       `buaa_id`,
                       `name`,
                       `password`,
                       `gender`,
                       `school`,
                       `is_ta`,
                       `is_teacher`,
                       `avatar`)
            VALUES (
                #{buaaId},
                #{name},
                #{password},
                #{gender},
                #{school},
                #{isTa},
                #{isTeacher},
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
}
