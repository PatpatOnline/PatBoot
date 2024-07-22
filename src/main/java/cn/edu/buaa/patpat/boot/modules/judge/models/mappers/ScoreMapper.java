package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Score;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScoreMapper {
    @Insert("""
            INSERT INTO `score` (`problem_id`, `account_id`, `score`, `created_at`, `updated_at`)
            VALUES (#{problemId}, #{accountId}, #{score}, #{createdAt}, #{updatedAt})
            ON DUPLICATE KEY UPDATE `score` = GREATEST(`score`, #{score}), `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrUpdate(Score score);

    @Insert("""
            INSERT INTO `score` (`problem_id`, `account_id`, `score`, `created_at`, `updated_at`)
            VALUES (#{problemId}, #{accountId}, #{score}, #{createdAt}, #{updatedAt})
            ON DUPLICATE KEY UPDATE `score` = #{score}, `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrForceUpdate(Score score);
}
