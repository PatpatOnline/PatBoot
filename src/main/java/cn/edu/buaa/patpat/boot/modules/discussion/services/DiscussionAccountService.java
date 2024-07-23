package cn.edu.buaa.patpat.boot.modules.discussion.services;

import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.DiscussionAccountMapper;
import cn.edu.buaa.patpat.boot.modules.discussion.models.views.DiscussionAccountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
