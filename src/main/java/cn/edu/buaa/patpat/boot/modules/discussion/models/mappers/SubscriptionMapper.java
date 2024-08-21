package cn.edu.buaa.patpat.boot.modules.discussion.models.mappers;

import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Subscription;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubscriptionMapper {
    @Insert("""
            INSERT IGNORE INTO `subscription` (`account_id`, `discussion_id`, `buaa_id`)
            VALUES (#{accountId}, #{discussionId}, #{buaaId})
            """)
    void subscribe(Subscription subscription);

    @Delete("""
            DELETE FROM `subscription`
            WHERE `account_id` = #{accountId} AND `discussion_id` = #{discussionId}
            """)
    void unsubscribe(int accountId, int discussionId);

    @Select("SELECT `account_id`, `buaa_id` FROM `subscription` WHERE `discussion_id` = #{discussionId}")
    List<Subscription> findSubscribers(int discussionId);
}
