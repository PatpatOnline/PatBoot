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
    private final DiscussionAccountService discussionAccountService;

    /**
     * Get all replies in a discussion.
     * This method uses some fancy stream operations to build the nested structure.
     */
    public List<ReplyView> query(int discussionId, int accountId) {
        List<ReplyView> flatReplies = replyFilterMapper.query(discussionId, accountId);
        if (flatReplies.isEmpty()) {
            return flatReplies;
        }

        Set<Integer> authorIds = flatReplies.stream()
                .map(ReplyView::getAuthorId)
                .collect(Collectors.toSet());
        Map<Integer, DiscussionAccountView> authorMap = discussionAccountService
                .getAll(authorIds).stream()
                .map(badge -> Map.entry(badge.getId(), badge))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        flatReplies.forEach(reply -> reply.setAuthor(authorMap.get(reply.getAuthorId())));
        Map<Integer, String> toNameMap = flatReplies.stream()
                .collect(Collectors.toMap(ReplyView::getId, ReplyView::getAuthorName));
        flatReplies.forEach(reply -> reply.setToName(toNameMap.get(reply.getToId())));

        return flatReplies.stream()
                .filter(reply -> reply.getParentId() == 0)
                .peek(reply -> {
                    List<ReplyView> children = flatReplies.stream()
                            .filter(child -> child.getParentId() == reply.getId())
                            .collect(Collectors.toList());
                    reply.setReplies(children);
                })
                .toList();
    }

    public Reply create(CreateReplyRequest request, int accountId) {
        Reply reply = mappers.map(request, Reply.class);
        if (request.getToId() != 0) {
            Reply target = replyMapper.findTo(request.getDiscussionId(), request.getToId());
            if (target == null) {
                throw new NotFoundException(M("reply.exists.not"));
            }
            if (target.getParentId() == 0) {
                reply.setParentId(target.getId());
            } else {
                reply.setParentId(target.getParentId());
            }
        }
        reply.setAuthorId(accountId);
        replyMapper.save(reply);
        return reply;
    }

    public Reply update(int id, UpdateReplyRequest request, int courseId, AuthPayload auth) {
        Reply reply = replyMapper.findUpdate(id);
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
        Reply reply = replyMapper.findDelete(id);
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

    public ReplyView get(int replyId, int accountId) {
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

    public void like(int id, int courseId, int accountId, boolean liked) {
        Reply reply = replyMapper.findLike(id);
        if (reply == null) {
            throw new NotFoundException(M("reply.exists.not"));
        }
        if (!discussionService.exists(courseId, reply.getDiscussionId())) {
            throw new NotFoundException(M("discussion.exists.not"));
        }

        if (liked) {
            replyMapper.like(id, accountId);
        } else {
            replyMapper.unlike(id, accountId);
        }
    }

    public void verify(int id, boolean verified) {
        int updated = replyMapper.updateVerified(id, verified);
        if (updated == 0) {
            throw new NotFoundException(M("reply.exists.not"));
        }
    }
}
