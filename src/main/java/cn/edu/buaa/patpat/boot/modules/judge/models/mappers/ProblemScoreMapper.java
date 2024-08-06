package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import cn.edu.buaa.patpat.boot.modules.judge.models.entities.ProblemScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemScoreMapper {
    @Insert("""
            INSERT INTO `problem_score` (`problem_id`, `account_id`, `score`, `created_at`, `updated_at`)
            VALUES (#{problemId}, #{accountId}, #{score}, #{createdAt}, #{updatedAt})
            ON DUPLICATE KEY UPDATE `score` = GREATEST(`score`, #{score}), `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrUpdate(ProblemScore score);

    @Insert("""
            INSERT INTO `problem_score` (`problem_id`, `account_id`, `score`, `created_at`, `updated_at`)
            VALUES (#{problemId}, #{accountId}, #{score}, #{createdAt}, #{updatedAt})
            ON DUPLICATE KEY UPDATE `score` = #{score}, `updated_at` = VALUES(`updated_at`)
            """)
    void saveOrForceUpdate(ProblemScore score);
}
