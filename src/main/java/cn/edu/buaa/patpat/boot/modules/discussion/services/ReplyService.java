package cn.edu.buaa.patpat.boot.modules.discussion.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.CreateReplyRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.UpdateReplyRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Reply;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionAccountMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.ReplyFilterMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.ReplyMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionAccountView;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.ReplyView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class ReplyService extends BaseService {
    private final ReplyMapper replyMapper;
    private final DiscussionAccountMapper discussionAccountMapper;
    private final ReplyFilterMapper replyFilterMapper;
    private final DiscussionService discussionService;

    /**
     * Get all replies in a discussion.
     * This method uses some fancy stream operations to build the nested structure.
     */
    public List<ReplyView> getAllInDiscussion(int discussionId, int accountId) {
        List<ReplyView> replies = replyFilterMapper.getAll(discussionId, accountId);
        if (replies.isEmpty()) {
            return replies;
        }

        Set<Integer> authorIds = replies.stream()
                .map(ReplyView::getAuthorId)
                .collect(Collectors.toSet());
        Map<Integer, DiscussionAccountView> authorMap = discussionAccountMapper
                .getAll(authorIds).stream()
                .map(badge -> Map.entry(badge.getId(), badge))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        replies.forEach(reply -> reply.setAuthor(authorMap.get(reply.getAuthorId())));

        return replies;
    }

    public Reply create(CreateReplyRequest request, int accountId) {
        Reply reply = mappers.map(request, Reply.class);
        reply.setAuthorId(accountId);
        replyMapper.save(reply);
        return reply;
    }

    public Reply update(int id, UpdateReplyRequest request, int courseId, AuthPayload auth) {
        Reply reply = replyFilterMapper.findUpdate(id);
        if (reply == null) {
            throw new NotFoundException(M("reply.exists.not"));
        }
        if (!discussionService.exists(courseId, reply.getDiscussionId())) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        if (reply.getAuthorId() != auth.getId() && auth.isStudent()) {
            throw new ForbiddenException(M("reply.update.forbidden"));
        }
        mappers.map(request, reply);
        replyMapper.update(reply);

        return reply;
    }

    public void delete(int id, int courseId, AuthPayload auth) {
        Reply reply = replyFilterMapper.findDelete(id);
        if (reply == null) {
            throw new NotFoundException(M("reply.exists.not"));
        }
        if (!discussionService.exists(courseId, reply.getDiscussionId())) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        if (reply.getAuthorId() != auth.getId() && auth.isStudent()) {
            throw new ForbiddenException(M("reply.delete.forbidden"));
        }
        replyMapper.deleteById(id);
    }

    public ReplyView detail(int replyId, int accountId) {
        var reply = replyFilterMapper.find(replyId, accountId);
        if (reply == null) {
            throw new NotFoundException(M("reply.exists.not"));
        }
        var badge = discussionAccountMapper.find(reply.getAuthorId());
        reply.setAuthor(badge);
        return reply;
    }

    public boolean exists(int discussionId, int replyId) {
        return replyFilterMapper.exists(discussionId, replyId);
    }
}
