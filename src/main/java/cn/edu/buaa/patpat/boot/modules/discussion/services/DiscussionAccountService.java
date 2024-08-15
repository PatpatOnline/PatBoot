package cn.edu.buaa.patpat.boot.modules.discussion.services;

import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionAccountMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionAccountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionAccountService {
    private final DiscussionAccountMapper discussionAccountMapper;
    private final BucketApi bucketApi;

    public DiscussionAccountView findBadge(int authorId) {
        var badge = discussionAccountMapper.find(authorId);
        if (badge != null) {
            badge.setAvatar(bucketApi.recordToUrl(badge.getAvatar()));
        }
        return badge;
    }

    public List<DiscussionAccountView> getAll(Iterable<Integer> ids) {
        var badges = discussionAccountMapper.getAll(ids);
        badges.forEach(badge -> badge.setAvatar(bucketApi.recordToUrl(badge.getAvatar())));
        return badges;
    }
}
