package cn.edu.buaa.patpat.boot.modules.discussion.services;

import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionAccountMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.ReplyMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionAccountView;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.ReplyView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReplyService {
    private final ReplyMapper replyMapper;
    private final DiscussionAccountMapper discussionAccountMapper;

    public ReplyService(ReplyMapper replyMapper, DiscussionAccountMapper discussionAccountMapper) {
        this.replyMapper = replyMapper;
        this.discussionAccountMapper = discussionAccountMapper;
    }

    /**
     * Get all replies in a discussion.
     * This method uses some fancy stream operations to build the nested structure.
     */
    public List<ReplyView> getAllInDiscussion(int discussionId, int accountId) {
        List<ReplyView> replies = replyMapper.getAllByDiscussionId(discussionId, accountId);
        if (replies.isEmpty()) {
            return replies;
        }

        Set<Integer> authorIds = replies.stream()
                .map(ReplyView::getAuthorId)
                .collect(Collectors.toSet());
        Map<Integer, DiscussionAccountView> authorMap = discussionAccountMapper
                .findByIds(authorIds).stream()
                .map(badge -> Map.entry(badge.getId(), badge))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        replies.forEach(reply -> reply.setAuthor(authorMap.get(reply.getAuthorId())));

        return replies;
    }
}
