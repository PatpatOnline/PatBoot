package cn.edu.buaa.patpat.boot.modules.discussion.services;

import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.CreateDiscussionRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.UpdateDiscussionRequest;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Discussion;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionFilter;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionFilterMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.ReplyMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionAccountView;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscussionService extends BaseService {
    private final DiscussionMapper discussionMapper;
    private final DiscussionFilterMapper discussionFilterMapper;
    private final ReplyMapper replyMapper;
    private final DiscussionAccountService discussionAccountService;

    public Discussion create(CreateDiscussionRequest request, int courseId, int accountId) {
        Discussion discussion = mappers.map(request, Discussion.class);
        discussion.setCourseId(courseId);
        discussion.setAuthorId(accountId);
        discussionMapper.save(discussion);
        return discussion;
    }

    public Discussion update(int id, UpdateDiscussionRequest request, int courseId, AuthPayload auth) {
        Discussion discussion = discussionMapper.findUpdate(courseId, id);
        if (discussion == null) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        if (discussion.getAuthorId() != auth.getId() && auth.isStudent()) {
            throw new ForbiddenException(M("discussion.update.forbidden"));
        }
        mappers.map(request, discussion);
        discussionMapper.update(discussion);

        return discussion;
    }

    public DiscussionView get(int courseId, int discussionId, int accountId) {
        var discussion = discussionFilterMapper.find(courseId, discussionId, accountId);
        if (discussion == null) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        var badge = discussionAccountService.findBadge(discussion.getAuthorId());
        discussion.setAuthor(badge);
        return discussion;
    }

    public Discussion delete(int courseId, int discussionId, AuthPayload auth) {
        Discussion discussion = discussionMapper.findDelete(courseId, discussionId);
        if (discussion == null) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
        if (discussion.getAuthorId() != auth.getId() && auth.isStudent()) {
            throw new ForbiddenException(M("discussion.delete.forbidden"));
        }
        discussionMapper.delete(discussionId);
        int cnt = replyMapper.deleteByDiscussionId(discussionId);
        log.info("Deleted discussion {} and its {} replie(s)", discussionId, cnt);

        return discussion;
    }

    public boolean exists(int courseId, int discussionId) {
        return discussionFilterMapper.exists(courseId, discussionId);
    }

    public void top(int id, boolean topped) {
        int updated = discussionMapper.updateTopped(id, topped);
        if (updated == 0) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
    }

    public void star(int id, boolean starred) {
        int updated = discussionMapper.updateStarred(id, starred);
        if (updated == 0) {
            throw new NotFoundException(M("discussion.exists.not"));
        }
    }

    public void like(int id, int accountId, boolean liked) {
        if (liked) {
            discussionMapper.like(id, accountId);
        } else {
            discussionMapper.unlike(id, accountId);
        }
    }

    public PageListDto<DiscussionView> query(int courseId, int accountId, int page, int pageSize, DiscussionFilter filter) {
        int count = discussionFilterMapper.count(courseId, filter);
        List<DiscussionView> discussions = count == 0
                ? List.of()
                : discussionFilterMapper.query(courseId, accountId, page, pageSize, filter);
        fillBadges(discussions);
        return PageListDto.of(discussions, count, page, pageSize);
    }

    private void fillBadges(List<DiscussionView> discussions) {
        Set<Integer> authorIds = discussions.stream()
                .map(DiscussionView::getAuthorId)
                .collect(Collectors.toSet());
        Map<Integer, DiscussionAccountView> authorMap = discussionAccountService
                .getAll(authorIds).stream()
                .map(badge -> Map.entry(badge.getId(), badge))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        discussions.forEach(discussion -> discussion.setAuthor(authorMap.get(discussion.getAuthorId())));
    }
}
