package cn.edu.buaa.patpat.boot.modules.discussion.services;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.discussion.dto.DiscussionNotification;
import cn.edu.buaa.patpat.boot.modules.discussion.models.entities.Subscription;
import cn.edu.buaa.patpat.boot.modules.discussion.models.mappers.SubscriptionMapper;
import cn.edu.buaa.patpat.boot.modules.message.api.MessageApi;
import cn.edu.buaa.patpat.boot.modules.message.dto.MessagePayload;
import cn.edu.buaa.patpat.boot.modules.message.dto.MessageWrapper;
import cn.edu.buaa.patpat.boot.modules.stream.api.StreamApi;
import cn.edu.buaa.patpat.boot.modules.stream.dto.WebSocketPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService extends BaseService {
    private final SubscriptionMapper subscriptionMapper;
    private final MessageApi messageApi;
    private final StreamApi streamApi;

    public void subscribe(Subscription subscription) {
        subscriptionMapper.subscribe(subscription);
    }

    public void unsubscribe(int accountId, int discussionId) {
        subscriptionMapper.unsubscribe(accountId, discussionId);
    }

    public void notifySubscribers(int courseId, int discussionId, int replyId, AuthPayload auth) {
        List<Subscription> subscribers = findSubscribers(discussionId);
        var data = new DiscussionNotification(discussionId, replyId, auth.getBuaaId(), auth.getName());

        // send messages to all subscribers
        List<MessageWrapper> wrappers = new ArrayList<>();
        MessagePayload<?> payload = MessagePayload.of(Globals.MSG_DISCUSSION, data);
        for (var subscriber : subscribers) {
            if (subscriber.getAccountId() == auth.getId()) {
                continue;
            }
            wrappers.add(MessageWrapper.of(courseId, subscriber.getAccountId(), payload));
        }
        messageApi.broadcast(wrappers);

        // real-time notification
        List<String> tags = subscribers.stream().map(Subscription::getBuaaId).toList();
        streamApi.broadcast(tags, WebSocketPayload.of(Globals.WS_MESSAGE, data));
    }

    private List<Subscription> findSubscribers(int discussionId) {
        return subscriptionMapper.findSubscribers(discussionId);
    }
}
