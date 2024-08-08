package cn.edu.buaa.patpat.boot.modules.judge.models.mappers;

import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import cn.edu.buaa.patpat.boot.modules.judge.models.views.SubmissionListView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SubmissionFilterMapper {
    @Select("""
            SELECT * FROM `submission`
            WHERE `problem_id` = #{problemId} AND `account_id` = #{accountId}
            ORDER BY `id` DESC
            LIMIT 1
            """)
    Submission findLast(int problemId, int accountId);

    @Select("SELECT * FROM `submission` WHERE `id` = #{id} AND `course_id` = #{courseId}")
    Submission findById(int id, int courseId);

    @SelectProvider(type = MapperProvider.class, method = "count")
    int count(int courseId, SubmissionFilter filter);

    @SelectProvider(type = MapperProvider.class, method = "query")
    List<SubmissionListView> queryImpl(int courseId, int pageSize, int offset, SubmissionFilter filter);

    @Select("""
            SELECT `id` FROM `account`
            WHERE `name` LIKE CONCAT('%', #{name}, '%')
            """)
    List<Integer> queryAccountIds(String name);

    @Select("""
            SELECT `s`.`id`, `s`.`account_id`, `s`.`language`, `s`.`score`,
                   `s`.`submit_time`, `s`.`start_time`, `s`.`end_time`,
                   `s`.`buaa_id`, `a`.`name`, `s`.`problem_id`, `p`.`problem_name`
            FROM (
                SELECT `id`, `account_id`, `buaa_id`, `problem_id`, `language`,
                `score`, `submit_time`, `start_time`, `end_time`
                FROM `submission`
                WHERE `id` = #{id} AND `course_id` = #{courseId}
            ) AS `s` JOIN (
                SELECT `id`, `name` FROM `account`
            ) AS `a` ON `s`.`account_id` = `a`.`id` JOIN (
                SELECT `id`, `title` AS `problem_name` FROM `problem`
            ) AS `p` ON `s`.`problem_id` = `p`.`id`
            """)
    SubmissionListView queryById(int id, int courseId);

    default List<SubmissionListView> query(int courseId, int page, int pageSize, SubmissionFilter filter) {
        return queryImpl(courseId, pageSize, (page - 1) * pageSize, filter);
    }
}
