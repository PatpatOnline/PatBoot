package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface DiscussionFilterMapper {
    @Select("""
            SELECT `id`, `type`, `author_id`, `course_id`, `title`, `content`,
                    `topped`, `starred`, `created_at`, `updated_at`,
                    (SELECT COUNT(*) FROM `like_discussion` WHERE `discussion_id` = #{discussionId}) AS `like_count`,
                    EXISTS(SELECT 1 FROM `like_discussion` WHERE `account_id` = #{accountId} AND `discussion_id` = #{discussionId}) AS `liked`,
                    (SELECT COUNT(*) FROM `reply` WHERE `discussion_id` = #{discussionId}) AS `reply_count`,
                    EXISTS(SELECT 1 FROM `subscription` WHERE `account_id` = #{accountId} AND `discussion_id` = #{discussionId}) AS `subscribed`
            FROM `discussion`
            WHERE `id` = #{discussionId} AND `course_id` = #{courseId}
            """)
    DiscussionView find(int courseId, int discussionId, int accountId);


    @Select("SELECT COUNT(*) FROM `discussion` WHERE `id` = #{discussionId} AND `course_id` = #{courseId}")
    boolean exists(int courseId, int discussionId);

    @SelectProvider(type = MapperProvider.class, method = "count")
    int count(int courseId, DiscussionFilter filter);

    @SelectProvider(type = MapperProvider.class, method = "query")
    List<DiscussionView> queryImpl(int courseId, int accountId, int pageSize, int offset, DiscussionFilter filter);

    default List<DiscussionView> query(int courseId, int accountId, int page, int pageSize, DiscussionFilter filter) {
        return queryImpl(courseId, accountId, pageSize, (page - 1) * pageSize, filter);
    }
}
